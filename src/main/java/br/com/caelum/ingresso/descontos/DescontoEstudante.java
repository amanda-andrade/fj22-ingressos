package br.com.caelum.ingresso.descontos;

import java.math.BigDecimal;

public class DescontoEstudante implements Desconto{

	@Override
	public BigDecimal aplicaDesconto(BigDecimal preco) {
		return preco.multiply(BigDecimal.valueOf(0.5));
	}
	
	@Override
	public String getDescricao() {
		return "Desconto estudante!";
	}
	

}
