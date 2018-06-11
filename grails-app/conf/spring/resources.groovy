import org.springframework.security.web.context.NullSecurityContextRepository
import org.springframework.security.web.context.SecurityContextPersistenceFilter

beans = {
    statelessSecurityContextRepository(NullSecurityContextRepository)
    statelessSecurityContextPersistenceFilter(SecurityContextPersistenceFilter, ref('statelessSecurityContextRepository'))
}
