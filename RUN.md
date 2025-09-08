# Marketplace API

## Descrição
API de marketplace desenvolvida em Java com Spring Boot para gerenciamento de produtos, avaliações e perguntas.

## Requisitos
- Java 17+
- Maven 3.6+

## Como Executar

1. Clone o repositório
2. Execute os comandos:
```bash
./mvnw clean install
./mvnw spring:boot run
```

Para Windows, use `mvnw.cmd` ao invés de `./mvnw`.

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/br/com/productshop/marketplace_api/
│   │   ├── application/
│   │   │   ├── dto/        # DTOs de Request/Response
│   │   │   ├── mapper/     # Mapeadores
│   │   │   ├── usecase/    # Casos de uso
│   │   │   └── service/    # Serviços
│   │   └── domain/
│   │       └── entity/     # Entidades de domínio
│   └── resources/
│       └── products.json   # Dados de exemplo
└── test/
    └── java/              # Testes unitários
```

## Funcionalidades

### Produtos

1. **Listar Produtos**
   - `GET /products`
   - Lista todos os produtos cadastrados

2. **Buscar Produto por ID**
   - `GET /products/{id}`
   - Retorna detalhes de um produto específico

3. **Produtos Similares**
   - `GET /products/{id}/similar`
   - Lista produtos similares
   - Query params:
     - `limit`: Limite de produtos retornados

4. **Filtrar por Categoria**
   - `GET /products/category/{category}`
   - Lista produtos de uma categoria específica

5. **Produtos por Vendedor**
   - `GET /products/seller/{sellerId}`
   - Lista produtos de um vendedor
   - Query params:
     - `excludeProduct`: ID do produto a excluir da lista

6. **Busca com Filtros**
   - `GET /products/search`
   - Query params:
     - `minPrice`: Preço mínimo
     - `maxPrice`: Preço máximo
     - `category`: Categoria
     - `minRating`: Avaliação mínima

## Testes

O projeto possui uma extensa cobertura de testes unitários. Para executar:

```bash
./mvnw test
```

### Cobertura de Testes (JaCoCo)

Para gerar e visualizar o relatório de cobertura:

1. Execute:
```bash
./mvnw clean test jacoco:report
```

2. Abra no navegador:
   - `target/site/jacoco/index.html`

O relatório mostra:
- Cobertura por pacote/classe
- Linhas cobertas (verde) e não cobertas (vermelho)
- Métricas de:
  - Cobertura de linhas
  - Cobertura de branches
  - Complexidade ciclomática

### Classes Testadas
- DTOs
  - ProductResponse
  - QuestionResponse
  - ReviewResponse
- Mappers
  - ProductMapper
- Services
  - ProductService
- Casos de Uso
  - FindByCategoryUseCase
  - FindBySellerUseCase
  - FindProductUseCase
  - FindSimilarProductsUseCase
  - ListProductsUseCase
  - SearchProductsUseCase
- Entidades
  - Product

### Classes Não Testadas e Motivos

#### DTOs não testados
1. **Request DTOs**
   - Validação já é feita pelo Spring Boot validation
   - São classes simples de transferência de dados
   - Não contêm lógica de negócio

2. **DTOs internos**
   - São usados apenas como estruturas de dados
   - Não possuem comportamento complexo
   - São validados indiretamente pelos testes de integração

#### Entidades não testadas
1. **Entidades simples**
   - Classes que são puramente estruturais
   - Não possuem lógica de negócio complexa
   - São validadas através dos testes de caso de uso

#### Estratégia de Testes
O projeto segue uma estratégia de testes focada em:
1. **Cobertura de Casos de Uso**
   - Onde está a lógica de negócio principal
   - Validações complexas
   - Regras de negócio

2. **Testes de Serviços**
   - Integração entre componentes
   - Fluxos completos de funcionalidades

3. **Testes de Mapeadores**
   - Conversão entre DTOs e entidades
   - Regras de transformação de dados

4. **Testes de DTOs e Entidades Complexas**
   - Apenas quando possuem lógica própria
   - Quando têm validações específicas
   - Quando implementam regras de negócio

## Tecnologias

- Spring Boot
- JUnit 5
- Mockito
- JaCoCo
- Maven

## Metas de Qualidade

- Cobertura de código:
  - 80% de cobertura de linhas
  - 70% de cobertura de branches
- Testes unitários para todas as funcionalidades
- Documentação atualizada
