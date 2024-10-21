const API_BASE_URL = 'http://localhost:8080/api';

// Inicializar tipos al cargar la página
document.addEventListener('DOMContentLoaded', () => {
  fetchTiposProducto(); // Corrección: función renombrada correctamente
  inicializarFormulario();
  
  // Inicializa el evento para filtrar productos
	const buscarCodigoInput = document.getElementById('buscar-codigo');
  	buscarCodigoInput.addEventListener('input', filtrarTiposProducto);
});

// Inicializar evento del formulario
function inicializarFormulario() {
  const tipoProductoForm = document.getElementById('tipo-producto-form');

  // Registrar el evento 'submit' del formulario
  tipoProductoForm.addEventListener('submit', async (event) => {
    event.preventDefault(); // Evitar recarga de página

    const nuevoTipoProducto = {
      codigo: document.getElementById('tipo-codigo').value,
      descripcion: document.getElementById('tipo-descripcion').value,
      fechaInicio: new Date().toISOString().split('T')[0], // Fecha actual
	  fechaFin: document.getElementById('tipo-fecha-fin').value || '9999-12-31',
    };

    console.log(JSON.stringify(nuevoTipoProducto)); // Verifica la salida en consola

    // Llamada al backend
    await crearTipoProducto(nuevoTipoProducto);

    // Resetear formulario y actualizar la lista
    tipoProductoForm.reset();
    fetchTiposProducto(); // Llamada correcta después de crear
  });
}

// Crear un tipo de producto nuevo
async function crearTipoProducto(tipoProducto) {
  try {
    const response = await fetch(`${API_BASE_URL}/tipos-producto`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(tipoProducto),
    });

    if (!response.ok) {
      const errorData = await response.json();
      if (response.status === 409) {
        alert(`Error: ${errorData.message}`);
      } else {
        alert(`Error inesperado: ${errorData.message || 'No se pudo crear el tipo producto.'}`);
      }
      return;
    }

    alert('Tipo producto creado exitosamente.');
  } catch (error) {
    console.error('Error al crear el tipo producto:', error);
    alert('Error de conexión. No se pudo crear el tipo producto.');
  }
}

// Obtener y mostrar los tipos de productos
async function fetchTiposProducto() {
  try {
    const response = await fetch(`${API_BASE_URL}/tipos-producto`);
	if (!response.ok) {
	      throw new Error(`Error: ${response.status}`);
	    }
	    const tipos = await response.json();
		
		// Limpiar el campo de búsqueda al recargar la lista
		document.getElementById('buscar-codigo').value = '';	
			
	    renderTiposProducto(Array.isArray(tipos) ? tipos : []);
	  } catch (error) {
	    console.error('Error al cargar tipos de productos:', error);
	  }
}


// Renderizar tipos de producto en la pantalla
function renderTiposProducto(tiposProducto) {
  const list = document.getElementById('tipo-producto-list');
  list.innerHTML = '';

  tiposProducto.forEach((tipoProducto) => {
    const item = document.createElement('div');
    item.className = 'list-item';

    item.innerHTML = `
      <div>
        <strong>${tipoProducto.codigo}</strong> - ${tipoProducto.descripcion}<br>
		<small>Fecha Inicio: ${formatFecha(tipoProducto.fechaInicio)} | Fecha Fin: ${formatFecha(tipoProducto.fechaFin)}</small><br>
      </div>
	  <button onclick="deleteTipoProducto(${tipoProducto.id})">Eliminar</button>
	        <a href="modificar-tipo-producto.html?id=${tipoProducto.id}"><button>Modificar</button></a>
	      `;
    list.appendChild(item);
  });
}

// Formatear fecha como YYYY-MM-DD
function formatFecha(fecha) {
  return new Date(fecha).toISOString().split('T')[0];
}

// Eliminar tipo de producto
async function deleteTipoProducto(id) {
  if (confirm('¿Está seguro de que desea eliminar este producto?')) {
    try {
      const response = await fetch(`${API_BASE_URL}/tipos-producto/${id}`, { method: 'DELETE' });
      if (response.ok) {
        alert('Tipo producto eliminado exitosamente.');
        fetchTiposProducto(); // Actualizar lista
      }
    } catch (error) {
      console.error('Error al eliminar tipo producto:', error);
    }
  }
}

function filtrarTiposProducto() {
  const terminoBusqueda = document.getElementById('buscar-codigo').value.toLowerCase();
  const tiposProducto = document.querySelectorAll('#tipo-producto-list .list-item');

  tiposProducto.forEach((tipoProducto) => {
    const codigoTipoProducto = tipoProducto.querySelector('strong').textContent.toLowerCase();
    if (codigoTipoProducto.includes(terminoBusqueda)) {
      tipoProducto.style.display = ''; // Mostrar si coincide
    } else {
      tipoProducto.style.display = 'none'; // Ocultar si no coincide
    }
  });
}