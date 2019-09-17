package br.com.caelum.ingresso.validador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.caelum.ingresso.dao.SessaoDao;
import br.com.caelum.ingresso.model.Sessao;

@Component
public class GerenciadorDeSessao {
	
	@Autowired
	private SessaoDao sessaoDao;
	
	private List<Sessao> sessoesDaSala;
	
	public GerenciadorDeSessao(List<Sessao> sessoesDaSala) {
		this.sessoesDaSala = sessoesDaSala;
	}
	
	public boolean cabe(Sessao sessaoNova) {
		List<Sessao> sessoes = sessaoDao.findAll();
		
		if(terminaAmanha(sessaoNova)) {
			return false;
		}
		return sessoesDaSala.stream().noneMatch(sessaoExistente -> horarioIsConflitante(sessaoExistente, sessaoNova));
	}

	private boolean terminaAmanha(Sessao sessao) {
		LocalDateTime terminoSessaoNova = getTerminoDeSessaoComDiaDeHoje(sessao);
		LocalDateTime ultimoInstanteDeHoje = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		
		if(terminoSessaoNova.isAfter(ultimoInstanteDeHoje)) {
			return true;
		}
		return false;
	}
	
	private boolean horarioIsConflitante(Sessao sessaoExistente, Sessao sessaoNova) {
		LocalDateTime inicioSessaoExistente = getInicioDaSessaoDeHoje(sessaoExistente);
		LocalDateTime terminoSessaoExistente = getTerminoDeSessaoComDiaDeHoje(sessaoExistente);
		LocalDateTime inicioSessaoNova = getInicioDaSessaoDeHoje(sessaoNova);
		LocalDateTime terminoSessaNova = getTerminoDeSessaoComDiaDeHoje(sessaoNova);
				
		boolean sessaoNovaTerminaAntesDaExistente = terminoSessaNova.isBefore(inicioSessaoExistente);
		
		boolean sessaoNovaComecaDepoisDaExistente = terminoSessaoExistente.isBefore(inicioSessaoNova);
		
		if(sessaoNovaTerminaAntesDaExistente || sessaoNovaComecaDepoisDaExistente) {
			return false;
		}
		
		return true;
	}

	private LocalDateTime getTerminoDeSessaoComDiaDeHoje(Sessao sessao) {
		LocalDateTime inicioDaSessao = getInicioDaSessaoDeHoje(sessao);
		
		return inicioDaSessao.plus(sessao.getFilme().getDuracao());
	}

	private LocalDateTime getInicioDaSessaoDeHoje(Sessao sessao) {
		return LocalDateTime.of(LocalDate.now(), sessao.getHorario());
	}

	

}
