<h2>Digital Innovation: Expert class - Desenvolvimento de testes unitários para validar uma API REST de gerenciamento de pontos de horas.</h2>

Nesta live coding, vamos aprender a testar, unitariamente, uma API REST para o gerenciamento de pontos de horas. Vamos desenvolver testes unitários para validar o nosso sistema de gerenciamento de pontos de horas, e também apresentar os principais conceitos e vantagens de criar testes unitários com JUnit e Mockito. Além disso, vamos também mostrar como desenvolver funcionalidades da nossa API através da prática do TDD.

Durante a sessão, serão abordados os seguintes tópicos:

* Baixar um projeto através do Git para desenolver nossos testes unitários.
* Apresentação conceitual sobre testes: a pirâmide dos tipos de testes, e também a importância de cada tipo de teste durante o ciclo de desenvolvimento.
* Foco nos testes unitários: mostrar o porque é importante o desenvolvimento destes tipos de testes como parte do ciclo de desenvolvimento de software.
* Principais frameworks para testes unitários em Java: JUnit, Mockito e Hamcrest.
* Desenvolvimento de testes unitários para validação de funcionalides básicas: criação, atualização, listagem, consulta e exclusão.
* TDD: apresentação e exemplo prático.
* Swagger: documentação de API/REST.

Para executar o projeto no terminal, digite o seguinte comando:

```shell script
gradle bootRun 
```

Para executar a suíte de testes desenvolvida durante a live coding, basta executar o seguinte comando:

```shell script
gradle test
```

Após executar o comando acima, basta apenas abrir o seguinte endereço e visualizar a execução do projeto:

```
http://localhost:8081/api/v1/work_days
http://localhost:8081/api/v1/user_categories
http://localhost:8081/api/v1/date_types
http://localhost:8081/api/v1/access_levels
http://localhost:8081/api/v1/occurrences
http://localhost:8081/api/v1/locations
http://localhost:8081/api/v1/calendars
http://localhost:8081/api/v1/companies
http://localhost:8081/api/v1/users
http://localhost:8081/api/v1/movements
http://localhost:8081/api/v1/bank_of_hours

```

São necessários os seguintes pré-requisitos para a execução do projeto desenvolvido durante a aula:

* Java 11 ou versões superiores.
* Intellj IDEA Community Edition ou sua IDE favorita.
* Controle de versão GIT instalado na sua máquina.
* Muita vontade de aprender e compartilhar conhecimento :)

Abaixo, seguem links bem bacanas, sobre tópicos mencionados durante a aula:

* [Referência do Intellij IDEA Community, para download](https://www.jetbrains.com/idea/download)
* [Palheta de atalhos de comandos do Intellij](https://resources.jetbrains.com/storage/products/intellij-idea/docs/IntelliJIDEA_ReferenceCard.pdf)
* [Site oficial do Spring](https://spring.io/)
* [Site oficial JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
* [Site oficial Mockito](https://site.mockito.org/)
* [Site oficial Hamcrest](http://hamcrest.org/JavaHamcrest/)
* [Referências - testes em geral com o Spring Boot](https://www.baeldung.com/spring-boot-testing)
* [Referência para o padrão arquitetural REST](https://restfulapi.net/)
* [Referência pirâmide de testes - Martin Fowler](https://martinfowler.com/articles/practical-test-pyramid.html#TheImportanceOftestAutomation)
