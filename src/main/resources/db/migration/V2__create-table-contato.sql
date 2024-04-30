create table contatos(

    id bigint not null auto_increment,
    usuario_id bigint not null,
    nome varchar(100) not null,
    cpf varchar(11) not null,
    telefone varchar(20) not null,
    logradouro varchar(100) not null,
    numero varchar(20) not null,
    complemento varchar(30),
    bairro varchar(100) not null,
    cidade varchar(100) not null,
    estado varchar(20) not null,
    cep varchar(8) not null,
    latitude decimal(10, 8) not null,
    longitude decimal(11,8) not null,

    primary key(id)

);