¿Por qué marcamos atributos como private , que riesgo evitamos?
    R:Evitamos que los datos puedan ser corrompidos 


¿Cuál es la diferencia entre private , protected y public , ilustra con un ejemplo en tu codigo 
    R:Private es el nivel más seguro ya que ningun otro archivo del proyecto puede verlo directamente
    protected : Aqui solo tienen acceso las clases que esten dentro del misma carpeta y clases hijas en herencia

    public :Este tiene un acceso universas donde otra clase en cualquier parte del proyecto puede invicoarlo y usarlo

    Use public en mis clases como Miembro y Contrato
    protected lo use en el atributo folio_contrato y en cancelar_contrato

¿Qué validacion incluiste en un setter? Que pasa saie el valor recibido es invalido

    R:Valide que la estructura del telefono fuera correcta (tenga dis digitos) haciendolo un string para evaluar su tamaño.