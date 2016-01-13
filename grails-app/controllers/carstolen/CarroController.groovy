package carstolen



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CarroController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Carro.list(params), model:[carroInstanceCount: Carro.count()]
    }

    def show(Carro carroInstance) {
        respond carroInstance
    }

    def create() {
        respond new Carro(params)
    }

    //busca
    // Controle de Buscas
    def telaBuscar (){
        render (view: 'busca')
    }

    //buscas
    def buscar (Carro buscado){
        def car = Carro.findByLetraPlacaAndNumeroPlaca (letraPlaca== buscado || numeroPlaca==buscado)

        if(car){

            render(view: 'index',model: [alunoInstanceList: alunos])    //consegue ir para index
        }else {
            flash.error = "Nenhuma placa com esses dados foram encontradas"
            render(view: 'busca')     //volta para a mesma paginda de busca
        }

    }

    // auterado ate aki

    @Transactional
    def save(Carro carroInstance) {
        if (carroInstance == null) {
            notFound()
            return
        }

        if (carroInstance.hasErrors()) {
            respond carroInstance.errors, view:'create'
            return
        }

        carroInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'carro.label', default: 'Carro'), carroInstance.id])
                redirect carroInstance
            }
            '*' { respond carroInstance, [status: CREATED] }
        }
    }

    def edit(Carro carroInstance) {
        respond carroInstance
    }

    @Transactional
    def update(Carro carroInstance) {
        if (carroInstance == null) {
            notFound()
            return
        }

        if (carroInstance.hasErrors()) {
            respond carroInstance.errors, view:'edit'
            return
        }

        carroInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Carro.label', default: 'Carro'), carroInstance.id])
                redirect carroInstance
            }
            '*'{ respond carroInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Carro carroInstance) {

        if (carroInstance == null) {
            notFound()
            return
        }

        carroInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Carro.label', default: 'Carro'), carroInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'carro.label', default: 'Carro'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
