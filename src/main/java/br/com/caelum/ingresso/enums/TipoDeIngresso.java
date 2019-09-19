package br.com.caelum.ingresso.enums;

import java.math.BigDecimal;

import br.com.caelum.ingresso.descontos.Desconto;
import br.com.caelum.ingresso.descontos.DescontoCorrentista;
import br.com.caelum.ingresso.descontos.DescontoEstudante;
import br.com.caelum.ingresso.descontos.SemDesconto;

public enum TipoDeIngresso {
	
	INTEIRO(new SemDesconto()),
	ESTUDANTE (new DescontoEstudante()),
	CORRENTISTA (new DescontoCorrentista());
	
	private final Desconto desconto;
	
	TipoDeIngresso(Desconto desconto){
		this.desconto = desconto;
	}
	
	public BigDecimal aplicaDesconto(BigDecimal valor) {
		return desconto.aplicaDesconto(valor);
	}
	
	public String getDescricao() {
		return desconto.getDescricao();
	}

}
