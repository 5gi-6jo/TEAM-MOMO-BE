package sparta.team6.momo.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.team6.momo.model.Account;
import sparta.team6.momo.repository.AccountRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       return  accountRepository.findByEmail(email)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException(email + "을 찾을 수 없습니다"));
    }

    private MoMoUser createUser(Account account) {
        return new MoMoUser(account.getId(), account.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
