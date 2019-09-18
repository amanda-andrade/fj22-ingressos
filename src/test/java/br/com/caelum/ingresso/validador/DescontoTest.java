package br.com.caelum.ingresso.validador;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.ingresso.descontos.DescontoEstudante;
import br.com.caelum.ingresso.descontos.DescontoIdoso;
import br.com.caelum.ingresso.descontos.SemDesconto;
import br.com.caelum.ingresso.model.Filme;
import br.com.caelum.ingresso.model.Ingresso;
import br.com.caelum.ingresso.model.Sala;
import br.com.caelum.ingresso.model.Sessao;


public class DescontoTest {
	
	@Test
	public void naoDeveConcederDescontoParaIngressoNormal() {
		
		Sala sala = new Sala("Eldorado - IMAX", new BigDecimal("20.5"));
		Filme filme  = new Filme("Rogue One", Duration.ofMinutes(120), "Sci-Fi", new BigDecimal("12"));
		
		Sessao sessao = new Sessao(LocalTime.parse("10:00:00"), filme, sala);
		Ingresso ingresso = new Ingresso(sessao, new SemDesconto());
		
		BigDecimal precoEsperado = new BigDecimal("32.50");
		
		Assert.assertEquals(precoEsperado, ingresso.getPreco());
		
	}
	
	@Test
	public void deveConcederDescontoParaEstudante() {
		
		Sala sala = new Sala("Eldorado - IMAX", new BigDecimal("20.5"));
		Filme filme  = new Filme("Rogue One", Duration.ofMinutes(120), "Sci-Fi", new BigDecimal("12"));
		
		Sessao sessao = new Sessao(LocalTime.parse("10:00:00"), filme, sala);
		Ingresso ingresso = new Ingresso(sessao, new DescontoEstudante());
		
		BigDecimal precoEsperado = new BigDecimal("16.25");
		
		Assert.assertEquals(precoEsperado, ingresso.getPreco());
		
	}
	
	@Test
	public void deveConcederDescontoParaIdoso() {
		
		Sala sala = new Sala("Eldorado - IMAX", new BigDecimal("20.5"));
		Filme filme  = new Filme("Rogue One", Duration.ofMinutes(120), "Sci-Fi", new BigDecimal("12"));
		
		Sessao sessao = new Sessao(LocalTime.parse("10:00:00"), filme, sala);
		Ingresso ingresso = new Ingresso(sessao, new DescontoIdoso());
		
		BigDecimal precoEsperado = new BigDecimal("19.50");
		
		Assert.assertEquals(precoEsperado, ingresso.getPreco());
		
	}
	
	

}
