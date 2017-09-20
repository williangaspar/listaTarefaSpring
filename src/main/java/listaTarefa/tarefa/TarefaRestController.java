package listaTarefa.tarefa;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class TarefaRestController {

	@Autowired
	private TarefaService tarefaService;

	@GetMapping("/tarefas")
	public List<Tarefa> allTarefas(){
		return tarefaService.findAll();
	}
	
	@PostMapping("/save")
	public String saveTarefa(@RequestBody Tarefa tarefa){
		if (tarefa.getData() == null) {
			tarefa.setData(new Date());
		}
		
		tarefaService.save(tarefa);
		return "Tarefa salva!";
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteTarefa(@PathVariable int id){
		tarefaService.delete(id);
		return "Tarefa deletada!";
	}
}
