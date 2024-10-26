const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
  fetchDepositos();
  inicializarFormulario();
    
  // Inicializa el evento para filtrar productos
  const buscarCodigoInput = document.getElementById('buscar-codigo');
  buscarCodigoInput.addEventListener('input', filtrarDepositos);
 });

 // Inicializar evento del formulario
 function inicializarFormulario() {
   const depositoForm = document.getElementById('deposito-form');
   
   // Registrar el evento 'submit' del formulario
   depositoForm.addEventListener('submit', async (event) => {
     event.preventDefault();

     const nuevoDeposito = {
       codigo: document.getElementById('deposito-codigo').value,
       descripcion: document.getElementById('deposito-descripcion').value,
       fechaInicio: new Date().toISOString().split('T')[0], // Fecha actual
 	  fechaFin: document.getElementById('deposito-fecha-fin').value || '9999-12-31',
     };

 	console.log(JSON.stringify(nuevoDeposito)); // Verifica la salida en consola

 	  // Llamada al backend
     await crearDeposito(nuevoDeposito);
 	
 	// Resetear formulario y actualizar la lista
     depositoForm.reset();
     fetchDepositos();
   });
 }

 async function crearDeposito(deposito) {
   try {
     const response = await fetch(`${API_BASE_URL}/depositos`, {
       method: 'POST',
       headers: { 'Content-Type': 'application/json' },
       body: JSON.stringify(deposito),
     });

 	if (!response.ok) {
 	      const errorData = await response.json();
 	      if (response.status === 409) {
 	        alert(`Error: ${errorData.message}`);
 	      } else {
 	        alert(`Error inesperado: ${errorData.message || 'No se pudo crear el deposito.'}`);
 	      }
 	      return;
 	    }

 	    alert('Deposito creado exitosamente.');
 	  } catch (error) {
 	    console.error('Error al crear el deposito:', error);
 	    alert('Error de conexión. No se pudo crear el tipo producto.');
 	  }
 	}


 	// Obtener y mostrar los depositos
 async function fetchDepositos() {
   try {
     const response = await fetch(`${API_BASE_URL}/depositos`);
 	if (!response.ok) {
 		      throw new Error(`Error: ${response.status}`);
 		    }
 		    const depositos = await response.json();
 			
 			// Limpiar el campo de búsqueda al recargar la lista
 			document.getElementById('buscar-codigo').value = '';	
 				
 		    renderDepositos(Array.isArray(depositos) ? depositos : []);
 		  } catch (error) {
 		    console.error('Error al cargar depositos:', error);
 		  }
 	}
 	
 	// Renderizar tipos de producto en la pantalla
 function renderDepositos(depositos) {
   const list = document.getElementById('deposito-list');
   list.innerHTML = '';

   depositos.forEach(deposito => {
     const item = document.createElement('div');
     item.className = 'list-item';
     item.innerHTML = `
       <div>
         <strong>${deposito.codigo}</strong> - ${deposito.descripcion} <br>
         <small>Fecha Inicio: ${formatFecha(deposito.fechaInicio)} | Fecha Fin: ${formatFecha(deposito.fechaFin)}</small>
       </div>
       <button onclick="deleteDeposito(${deposito.id})">Eliminar</button>
       <a href="modificar-deposito.html?id=${deposito.id}"><button>Modificar</button></a>
     `;
     list.appendChild(item);
   });
 }

 function formatFecha(fecha) {
   return new Date(fecha).toISOString().split('T')[0];
 }

 async function deleteDeposito(id) {
   if (confirm('¿Está seguro de que desea eliminar este deposito?')) {
     try {
       const response = await fetch(`${API_BASE_URL}/depositos/${id}`, { method: 'DELETE' });
       if (response.ok) {
         alert('Deposito eliminado exitosamente.');
         fetchDepositos();
       }
     } catch (error) {
       console.error('Error al eliminar deposito:', error);
     }
   }
 }

 function filtrarDepositos() {
   const terminoBusqueda = document.getElementById('buscar-codigo').value.toLowerCase();
   const depositos = document.querySelectorAll('#deposito-list .list-item');

   depositos.forEach((deposito) => {
     const codigoDeposito = deposito.querySelector('strong').textContent.toLowerCase();
     if (codigoDeposito.includes(terminoBusqueda)) {
       deposito.style.display = ''; // Mostrar si coincide
     } else {
       deposito.style.display = 'none'; // Ocultar si no coincide
     }
   });
 }