package br.com.caelum.ingresso.descontos;

import java.math.BigDecimal;

public class SemDesconto implements Desconto{

	@Override
	public BigDecimal aplicaDesconto(BigDecimal preco) {
		return preco;
	}

	@Override
	public String getDescricao() {
		return "Normal";
	}
	
}
