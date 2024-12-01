package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.Roles;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Cart;
import com.siuuuuu.backend.entity.Role;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.CartRepository;
import com.siuuuuu.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CartRepository cartRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            account = new Account();
            Role customerRole = roleRepository.findByName(Roles.CUSTOMER.name());
            account.setEmail(email);
            account.setPassword("");
            account.getRoles().add(customerRole);
            accountRepository.save(account);

            Cart cart = new Cart();
            cart.setAccount(account);
            cartRepository.save(cart);
        }
        return new org.springframework.security.oauth2.core.user.DefaultOAuth2User(
                account.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()),
                oAuth2User.getAttributes(),
                "email"
        );
    }
}