package br.com.caelum.ingresso.rest;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.com.caelum.ingresso.model.DetalhesDoFilme;
import br.com.caelum.ingresso.model.Filme;

@Component
public class OmdbClient {

	public <T> Optional<T> pegaDetalhesDo(Filme filme, Class<T> tClass) {
				
		RestTemplate cliente = new RestTemplate();
		
		String nomeDoFilme = filme.getNome().replace(" ","+");
    	
    	String urlCompleta = String.format("https://omdb-fj22.herokuapp.com/movie?title=%s", nomeDoFilme);
    	
    	try {
    		
    		T detalhesGenericos = cliente.getForObject(urlCompleta, tClass);
    		return Optional.ofNullable(detalhesGenericos);
    		
		} catch (RestClientException e) {
			return Optional.empty();
		}
		
	}
}
