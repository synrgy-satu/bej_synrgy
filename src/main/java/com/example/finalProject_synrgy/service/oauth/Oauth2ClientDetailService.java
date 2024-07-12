package com.example.finalProject_synrgy.service.oauth;

import com.example.finalProject_synrgy.repository.oauth2.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class Oauth2ClientDetailService implements ClientDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        ClientDetails client = clientRepository.findOneByClientId(s);
        if (null == client) {
            throw new ClientRegistrationException("Client not found");
        }

        return client;
    }

}

