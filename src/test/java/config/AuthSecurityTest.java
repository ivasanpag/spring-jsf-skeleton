package config;

import com.ijsp.config.SecurityConfig;
import com.ijsp.config.WebApplicationConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.naming.NamingException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author ijsp
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        SecurityConfig.class,
        WebApplicationConfig.class
})
@WebAppConfiguration
public class AuthSecurityTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() throws NamingException {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }

    @Test
    public void fullAuthenticatePageWithNoAuthUser() throws Exception {
        mvc.perform(get("/faces/dashboard.xhtml").with(csrf().asHeader()))
                .andExpect(status().is(302))
                .andExpect(redirectedUrlPattern("**/faces/login.xhtml"));
    }

    @Test
    public void noAuthenticatePage() throws Exception {
        mvc.perform(get("/faces/login.xhtml").with(csrf().asHeader()))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser // Role USER by default
    public void fullAuthenticatePageWithAuthenticatedUser() throws Exception {
        mvc.perform(get("/faces/dashboard.xhtml").with(csrf().asHeader()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void fullAuthenticatePageWithUserWithoutAdminRole() throws Exception {
        mvc.perform(get("/faces/setting.xhtml").with(csrf().asHeader()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void fullAuthenticateSettingPageWithAdminUser() throws Exception {
        mvc.perform(get("/faces/setting.xhtml").with(csrf().asHeader()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void fullAuthenticateDashboardPageWithAdminUser() throws Exception {
        mvc.perform(get("/faces/dashboard.xhtml").with(csrf().asHeader()))
                .andExpect(status().isOk());
    }

    @Test
    public void formLoginWithoutUser() throws Exception {
        mvc.perform(formLogin("/faces/login.xhtml"))
                .andExpect(unauthenticated());
    }

}