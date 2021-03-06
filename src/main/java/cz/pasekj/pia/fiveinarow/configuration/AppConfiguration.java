package cz.pasekj.pia.fiveinarow.configuration;

import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.DispatchType;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.SendStatus;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletContext;

/**
 * Application configuration for URL rewriting
 */
@Configuration
public class AppConfiguration extends HttpConfigurationProvider {

    @Override
    public org.ocpsoft.rewrite.config.Configuration getConfiguration(ServletContext context) {
        return ConfigurationBuilder.begin()
                .addRule().when(DispatchType.isRequest().and(Path.matches("/{path}")))
                .perform(SendStatus.code(404))
                .where("path").matches("^/jsf/.*\\.xhtml$")
                .addRule(Join.path("/").to("/jsf/index.xhtml"))
                .addRule(Join.path("/profile").to("/jsf/profile.xhtml"))
                .addRule(Join.path("/game").to("/jsf/game.xhtml"))
                .addRule(Join.path("/admin").to("/jsf/admin.xhtml"))
                .addRule(Join.path("/password/reset").to( "/jsf/password_reset.xhtml"))
                .addRule(Join.path("/password/change").to( "/jsf/password_recovery.xhtml"))
                .addRule(Join.path("/login").to("/jsf/login.xhtml"));
    }

    @Override
    public int priority() {
        return 10;
    }

    /** PasswordEncoder bean definition */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
