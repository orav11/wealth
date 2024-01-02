package com.wealth.rating;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wealth.rating.model.FinancialInfo;
import com.wealth.rating.model.Person;
import com.wealth.rating.model.PersonalInfo;
import com.wealth.rating.service.CentralBankService;
import com.wealth.rating.service.WealthRatingService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest
@ExtendWith({SpringExtension.class, OutputCaptureExtension.class})
class WealthRatingApplicationTests {


	private MockMvc mockMvc;

	@MockBean
	private CentralBankService centralBankService;

	@Autowired
	private WealthRatingService wealthRatingService;

	List<Person> rich = List.of(
			new Person(1, new FinancialInfo(new BigInteger("30000"), 10), new PersonalInfo("Donald", "T", "C")),
			new Person(2, new FinancialInfo(new BigInteger("50000"), 4), new PersonalInfo("Elon", "M", "D")));

	List<Person> poor = List.of(
			new Person(3, new FinancialInfo(new BigInteger("10000"), 10), new PersonalInfo("Avi", "B", "C")),
			new Person(4, new FinancialInfo(new BigInteger("10000"), 7), new PersonalInfo("Guy", "G", "C")));



	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

	}

	@AfterEach
	public void clean(){
		wealthRatingService.removeAll();
	}
	@Test
	public void testPersonIsRich() throws Exception {
		Mockito.when(centralBankService.getCityAssetEvaluation(Mockito.anyString())).thenReturn(new BigInteger("50000"));
		Mockito.when(centralBankService.getWealthThreshold()).thenReturn(new BigInteger("10000"));

		for(Person p : rich) {
			evaluatePersonWealth(p);
		}
		int numOfRich = wealthRatingService.getAllRichPeople().size();
		Assertions.assertEquals(rich.size(),numOfRich);
	}

	@Test
	public void testPersonIsPoor() throws Exception {
		Mockito.when(centralBankService.getCityAssetEvaluation(Mockito.anyString())).thenReturn(new BigInteger("5000"));
		Mockito.when(centralBankService.getWealthThreshold()).thenReturn(new BigInteger("100000"));

		for(Person p : poor) {
			evaluatePersonWealth(p);
		}
		int numOfRich = wealthRatingService.getAllRichPeople().size();
		Assertions.assertEquals(0,numOfRich);
	}

	@Test
	public void testPersonIdDoesNotExist(CapturedOutput output){

		assertThatThrownBy(() -> {
			wealthRatingService.getRichPersonById(123);
		}).isInstanceOf(EntityNotFoundException.class)
				.hasMessageContaining("Could not find person with id 123");

	}

	@Test
	public void testRichAndThenPoor(CapturedOutput output) throws Exception {

		Mockito.when(centralBankService.getCityAssetEvaluation(Mockito.anyString())).thenReturn(new BigInteger("1000"));
		Mockito.when(centralBankService.getWealthThreshold()).thenReturn(new BigInteger("10000"));

		//Person was rich and was saved to the db
		evaluatePersonWealth(rich.get(0));

		int size = wealthRatingService.getAllRichPeople().size();
		Assertions.assertEquals(1,size);

		//Person got poor and will be deleted from db
		rich.get(0).setFinancialInfo(new FinancialInfo(new BigInteger("1"),1));
		evaluatePersonWealth(rich.get(0));
		size = wealthRatingService.getAllRichPeople().size();
		Assertions.assertEquals(0,size);

	}


	private void evaluatePersonWealth(Person person) throws Exception {

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(person);

		mockMvc.perform( MockMvcRequestBuilders
				.post("http://localhost:8080/api/wealth-rating/analyse_status")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
	}
}
