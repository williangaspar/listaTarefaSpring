package listaTarefa.tarefa;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TarefaController {
	
	@Autowired
	private TarefaService tarefaService;
	
	@GetMapping("/")
	public String all(HttpServletRequest request){
		request.setAttribute("tarefas", tarefaService.findAll());
		return "index";
	}
}
