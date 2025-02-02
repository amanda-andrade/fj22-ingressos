package br.com.caelum.ingresso.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.caelum.ingresso.dao.FilmeDao;
import br.com.caelum.ingresso.dao.SalaDao;
import br.com.caelum.ingresso.dao.SessaoDao;
import br.com.caelum.ingresso.enums.TipoDeIngresso;
import br.com.caelum.ingresso.model.Carrinho;
import br.com.caelum.ingresso.model.ImagemDaCapa;
import br.com.caelum.ingresso.model.Sessao;
import br.com.caelum.ingresso.model.form.SessaoForm;
import br.com.caelum.ingresso.rest.OmdbClient;
import br.com.caelum.ingresso.validador.GerenciadorDeSessao;

@Controller
public class SessaoController {
	
	@Autowired
	private FilmeDao filmeDao;
	
	@Autowired
	private SalaDao salaDao;
	
	@Autowired
	private SessaoDao sessaoDao;
	
	@Autowired
	private Carrinho carrinho;
	
	@GetMapping("/sessao/{sessaoId}/lugares")
	public ModelAndView form(@PathVariable("sessaoId") Integer id) {
		ModelAndView modelAndView = new ModelAndView("sessao/lugares");
		 
		Sessao sessao = sessaoDao.findOne(id);
		
		OmdbClient cliente = new OmdbClient();
		
		Optional<ImagemDaCapa> detalhes = cliente.pegaDetalhesDo(sessao.getFilme(),ImagemDaCapa.class);
		
		modelAndView.addObject("sessao", sessao);
		modelAndView.addObject("carrinho", carrinho);
		modelAndView.addObject("imagemCapa", detalhes.orElse(new ImagemDaCapa()));
		modelAndView.addObject("tiposDeIngressos", TipoDeIngresso.values());
		
		return modelAndView;
	}
	
	@GetMapping("/admin/sessao")
	public ModelAndView form(@RequestParam("salaId") Integer salaId, SessaoForm form) {
		
		ModelAndView modelAndView = new ModelAndView("sessao/sessao");
		modelAndView.addObject("sala", salaDao.findOne(salaId));
		modelAndView.addObject("filmes", filmeDao.findAll());
		modelAndView.addObject("form", form);
		
		return modelAndView;
	}
	
	@PostMapping("/admin/sessao")
	@Transactional
	public ModelAndView salva(@Valid SessaoForm form, BindingResult result) {
		
		if(result.hasErrors()) return form(form.getSalaId(), form);
		
		Sessao sessao = form.toSessao(salaDao, filmeDao);
		
		List<Sessao> sessoesDaSala = sessaoDao.buscaSessoesDaSala(sessao.getSala());
		
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoesDaSala);
		
		if(gerenciador.cabe(sessao)) {
			
			sessaoDao.save(sessao);
			
			return new ModelAndView("redirect:/admin/sala/" + form.getSalaId()+"/sessoes");
		}
		
		result.rejectValue("horario", "error.sessionConflict", "Conflito com sessões existentes! Escolha outro horário.");
		
		
		return form(form.getSalaId(), form);
	}
}
