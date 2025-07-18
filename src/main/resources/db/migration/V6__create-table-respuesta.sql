create table respuesta(

    id int not null auto_increment,
    mensaje text not null,
    topico int not null,
    fechaCreacion datetime default current_timestamp not null,
    autor int not null,
    solucion boolean default false not null,

    foreign key(topico) references topico(id),
    foreign key(autor) references usuario(id),
    primary key(id)
);