# Exemplo Spring Boot Rest JPA JSP
Este exemplo tem como objetivo apresentar uma aplicação Spring Boot e suas características básicas.

## Sobre o Spring Boot

O [Spring Boot](https://projects.spring.io/spring-boot/) é um frame work web criado para facilitar o desenvolvimento de aplicações Java EE que fornece várias ferramentas e realiza uma grande quantidade de trabalho automatizado.
Com o Spring Boot é possível obter um jar executável ao contrário de arquivos war que são dependente de um servidor externo para funcionar.

## Sobre a aplicação
A aplicação consiste basicamente de uma página JSP, que é responsável por exibir uma lista de tarefas e uma API rest para a modificação da lista.

## Código

O primeiro arquivo a ser observado é o `pom.xml`. Nele estão presentes as configurações das dependências do projeto.

Em seguida temos o `application.properties` que aceita várias configurações para a aplicação, como porta de acesso, banco de dados e muito mais.

```bash
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
```

No código acima é especificado onde estará os arquivos da view e sua extensão, JSP neste caso.

Depois temos o `Main`.
```java
package listaTarefa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
```

Geralmente aplicações Java Web não possuem uma classe principal. Porém, graças a sua habilidade de funcionar como standalone, o spring boot possui está classe que é definida através da anotação `@SpringBootApplication` que informa para o Spring qual a classe principal do sistema.
Depois temos a chamada da função `SpringApplication.run`. Essa única linha é responsável por iniciar o servidor.

Em seguida temos o DAO fornecido pela JPA

```java
package listaTarefa.tarefa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Tarefa {
	@Id
	private int id;
	private String titulo;
	private String descricao;
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	private boolean finalizada;
	
	...
}
```
Não há nada do Spring aqui, mas sim o objeto de conexão com o banco de dados, que neste caso é apenas em memória e desaparece toda vez que a aplicação é terminada.

Aqui está uma mágica do Spring Boot. O código abaixo é apenas uma interface, porém o Spring irá criar a implementação de vários métodos para o CRUD no banco de dados como `save`, `delete`, `findAll` entre outros, tudo automaticamente.
```java
package listaTarefa.tarefa;

import org.springframework.data.repository.CrudRepository;

public interface TarefaRepository extends CrudRepository<Tarefa, Integer>{

}
```
A interface `TarefaRepository` será utilizada pelo `TarefaService`. A anotação `@Autowired` é responsável por injetar uma instância da interface com todos os métodos fornecidos pelo Spring.

```java
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
```
Finalmente chegado ao `controller`. O controller irá receber a chamada de URLs e enviar páginas HTML para o cliente.

A anotação `@GetMapping("/")` informa para o framework que temos uma escuta no get do endpoint /.

```java
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
```

O `request.setAttribute` injeta o objeto tarefas no JSP. Ao retornar a string "index", o controller informa ao spring para procurar um arquivo `index.jsp` na pasta definida no aquivo `application.properties`

```jsp
<c:choose>
	<c:when test="${tarefas.size() == 0}">
		<%-- ... --%>
	</c:when>
	<c:otherwise>
		<%-- ... --%>
		<c:forEach var="tarefa" items="${tarefas}">
		<%-- ... --%>
	</c:otherwise>
</c:choose>
```

Por último temos o `@RestController`. Muito similar ao controller porém focado em APIs REST, ou seja, JSON. O output dos  RestController são transformados em JSON.

```java
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

```

Logo abaixo da anotação do controller temos `@RequestMapping("rest")` está anotação está dizendo que todo os pedidos para /rest serão direcionados para este controller. Todos os mapeamentos seguintes irão ser /rest/alguma_coisa. Por exemplo `@GetMapping("/tarefas")` será localhost:8080/rest/tarefas.

Na função `saveTarefa` o Spring se encarrega de transformar o JSON recebido do client em um objeto do tipo Tarefa.

Na função `deleteTarefa` temos o parâmetro `{id}` ao final da URL. Ele é consumido pela função através da anotação `@PathVariable` seguido pela declaração da variável `int id`. O nome da variável deve ser idêntico ao nome do parâmetro para funcionar.

## Instalação
Há várias maneiras de por o projeto para funcionar. Aqui irei explicar como fazer a importação do fonte para o [Eclipse](https://www.eclipse.org/downloads/?), IDE utilizada para o desenvolvimento da aplicação.

* Após ter clonado ou feito o download abra o eclipse.
* Vá até o menu `File->Import`.
* Na janela que se abrir escolha ou digite `Existing Projects into Workspace` e depois `next`.
* Deixe tudo como está e clique no botão `Browser`.
* navegue até a pasta do projeto, confirme e finalize a importação clicando em `finish`.

Se tudo ocorreu bem o projeto está importando. Agora é preciso importar as dependência do [Maven](https://maven.apache.org/). Para isto:

* Selecione o projeto no `Project explorer`
* Clique como o botão direito do mouse e vá até `Maven->Update Project` 
*  Na janela que se abre, selecione listaTarefa e clique em `ok`.
*  Clique como botão direito do mouse sobre o projeto novamente e vá até `Maven->Download Sources`.

Se tudo ocorreu bem você já pode executar o projeto. Para isto:
 
* navegue até o arquivo `Main` dentro do pacote listaTarefa.
* Clique com o botão direito do mouse sobre o arquivo.
* Selecione a opção `Run as->Java Application`

Acompanhe na parte inferior da tela a inicialização do servidor. Caso sudo saia bem o site estará disponível em`localhost:8080`

## Testando CRUD rest

Recomendo que faça os testes com a ferramenta [PostMan](https://www.getpostman.com/).

### Exemplo de post para adicionar tarefa:

tipo de request: POST

url: `http://localhost:8080/rest/save`

Headers: 
Key = Content-Type 
Value = application/json

Body:
```json
{
	"id": 1,
	"titulo": "criar tutorial",
	"descricao": "criar um tutorial",
	"finalizada": true
}
```

FIM.

