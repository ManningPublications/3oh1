import com.maxmind.geoip.LookupService
import org.springframework.security.web.context.NullSecurityContextRepository
import org.springframework.security.web.context.SecurityContextPersistenceFilter

beans = {
    statelessSecurityContextRepository(NullSecurityContextRepository)
    statelessSecurityContextPersistenceFilter(SecurityContextPersistenceFilter, ref('statelessSecurityContextRepository'))

    geoIpService(LookupService, this.class.getClassLoader().getResource(grailsApplication.config.grails.plugin.geoip.data.path as String).file)
}
