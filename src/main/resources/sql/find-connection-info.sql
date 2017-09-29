select * from cliente_rjconsultores crj join cliente c
on(crj.id_cliente=c.id_cliente and c.url_zona=:#${property.urlZona})