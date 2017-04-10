insert into PONTOS_DE_ORIGEM(id,localidade,municipio,uf) values
(:#${body[id]},:#${body[localidade]},:#${body[municipio]},:#${body[uf]});