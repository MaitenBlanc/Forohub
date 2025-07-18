create table topico(

    id int not null auto_increment,
    titulo varchar(255) not null,
    mensaje text not null,
    fechaCreacion datetime default current_timestamp not null,
    status varchar(20) default 'ABIERTO' not null,
    autor int not null,
    curso int not null,
    respuestas int not null default 0,

    foreign key(autor) references usuario(id),
    foreign key(curso) references curso(id),
    primary key(id)
);