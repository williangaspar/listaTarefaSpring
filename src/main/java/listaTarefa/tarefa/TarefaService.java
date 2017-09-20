package listaTarefa.tarefa;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TarefaService {

	@Autowired
	private TarefaRepository tarefaRepository;
	 
	public List<Tarefa> findAll() {
		List<Tarefa> tarefas = new ArrayList<>();
		tarefaRepository.findAll().forEach(tarefas::add);
		return tarefas;
	}
	
	public void save(Tarefa tarefa) {
		tarefaRepository.save(tarefa);
	}

	public void delete(int id) {
		tarefaRepository.delete(id);
	}
}
