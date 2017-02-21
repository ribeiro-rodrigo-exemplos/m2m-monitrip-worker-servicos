update cliente_rjconsultores
set dt_sincronismo_servicos = DATE_ADD(:#${property.dtSincronismo}, INTERVAL 1 DAY)
where id_cliente = :#${property.idCliente}