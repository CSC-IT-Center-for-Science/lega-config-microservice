package eu.crg.ega.configservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.crg.ega.configservice.Application;
import eu.crg.ega.microservice.dto.Base;
import eu.crg.ega.microservice.dto.ServiceLocation;
import eu.crg.ega.microservice.enums.ServiceType;
import eu.crg.ega.microservice.test.util.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ConfigControllerTest {

  @Autowired
  private ConfigController controller;

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private ObjectMapper objectMapper;

  @Before
  public void setUp() {

    mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
        .alwaysExpect(content().contentType(TestUtils.APPLICATION_JSON_CHARSET_UTF_8))
        .build();
  }

  @Test
  public void obtainTypes() throws Exception {

    MvcResult
        mvcResult =
        mockMvc.perform(get("/services/types")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    Base<ServiceType> result = TestUtils.jsonToObject(mvcResult, ServiceType.class, objectMapper);

    assertThat(result.getResponse().getNumTotalResults(), equalTo(Arrays.asList(ServiceType.values()).size()));
  }

  @Test
  public void obtainServices() throws Exception {
    TestUtils.addAnonymousUserToContext();

    MvcResult
        mvcResult =
        mockMvc.perform(get("/services")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    Base<ServiceLocation> result = TestUtils.jsonToObject(mvcResult, ServiceLocation.class, objectMapper);

    assertThat(result.getResponse().getNumTotalResults(), equalTo(3));
  }

  @Test
  public void addNewService() throws Exception {
    TestUtils.addAdminUserToContext("userAdmin");

    String name = "name2sdkSDFk393mksSDFKM";

    String contentToPass = objectMapper.writeValueAsString(
        ServiceLocation.builder().name(name).port("333").server("182.188.99.9")
            .serviceId("serviceid1").baseUrl("").type(ServiceType.CONFIG).version("v1").build()
    );

    MvcResult
        mvcResult =
        mockMvc.perform(post("/services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(contentToPass)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    Base<ServiceLocation> result = TestUtils.jsonToObject(mvcResult, ServiceLocation.class, objectMapper);

    assertThat(result.getResponse().getNumTotalResults(), equalTo(1));
    assertThat(result.getResponse().getResult().get(0).getName(), equalTo(name));

    //Remove just added service
    MvcResult
        mvcResult2 =
        mockMvc.perform(delete("/services/serviceid1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

  }

  @Test
  public void obtainSpecificServices() throws Exception {
    TestUtils.addAnonymousUserToContext();

    //The configservice1 id comes from the FillRepo class
    MvcResult
        mvcResult =
        mockMvc.perform(get("/services/configservice1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    Base<ServiceLocation> result = TestUtils.jsonToObject(mvcResult, ServiceLocation.class, objectMapper);

    assertThat(result.getResponse().getNumTotalResults(), equalTo(1));
  }

}
