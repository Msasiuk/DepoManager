const API_BASE_URL = 'http://localhost:8080/api';

// Inicializar productos y tipos al cargar la página
document.addEventListener('DOMContentLoaded', () => {
  fetchProductos();
  fetchTiposProducto();
  inicializarFormulario();
});


// Inicializar evento del formulario
function inicializarFormulario() {
  const productoForm = document.getElementById('producto-form');
  
  // Registrar el evento 'submit' del formulario
  productoForm.addEventListener('submit', async (event) => {
    event.preventDefault(); // Evitar recarga de página

    // Validación de valores positivos
	const stockMaximo = parseInt(document.getElementById('producto-stock-max').value);
	const stockMinimo = parseInt(document.getElementById('producto-stock-min').value);
	const puntoReposicion = parseInt(document.getElementById('punto-reposicion').value);

	if (stockMaximo <= 0 || stockMinimo <= 0 || puntoReposicion <= 0) {
	      alert('Los stocks y el punto de reposición deben ser valores positivos.');
	      return;
	}
	
	const nuevoProducto = {
	      codigo: document.getElementById('producto-codigo').value,
	      descripcion: document.getElementById('producto-descripcion').value,
	      puntoReposicion: puntoReposicion,
	      stockMaximo: stockMaximo,
	      stockMinimo: stockMinimo,
	      tipoProducto: { id: parseInt(document.getElementById('producto-tipo').value) },
	      tieneVencimiento: document.getElementById('producto-tiene-vencimiento').value,
	      //fechaVencimiento: fechaVencimientoInput.value,
	      fechaInicio: new Date().toISOString().split('T')[0], // Fecha actual
		  fechaFin: document.getElementById('producto-fecha-fin').value || '9999-12-31',
	      cantidad: 0,
	};
	
	console.log(JSON.stringify(nuevoProducto)); // Verifica la salida en la consola

	// Llamada al backend
	await crearProducto(nuevoProducto);
    
    // Resetear el formulario y actualizar la lista de productos
    productoForm.reset();
    productoIdActual = null;
    fetchProductos();
  });
}

// Crear un producto nuevo
async function crearProducto(producto) {
  try {
    const response = await fetch(`${API_BASE_URL}/productos`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(producto),
    });

    if (!response.ok) {
      const errorData = await response.json();
      if (response.status === 409) {
        alert(`Error: ${errorData.message}`);
      } else {
        alert(`Error inesperado: ${errorData.message || 'No se pudo crear el producto.'}`);
      }
      return;
    }

    alert('Producto creado exitosamente.');
  } catch (error) {
    console.error('Error al crear el producto:', error);
    alert('Error de conexión. No se pudo crear el producto.');
  }
}

// Obtener los productos en la pantalla
async function fetchProductos() {
  try {
    const response = await fetch(`${API_BASE_URL}/productos`);
	if (!response.ok) {
	      throw new Error(`Error: ${response.status}`);
	    }
	    const productos = await response.json();
	    renderProductos(Array.isArray(productos) ? productos : []);
	  } catch (error) {
	    console.error('Error al cargar productos:', error);
	}
}

// Renderizar los productos para ser mostrados en pantalla
function renderProductos(productos) {
  const list = document.getElementById('producto-list');
  list.innerHTML = '';

  productos.forEach((producto) => {
	const tieneVencimiento = producto.tieneVencimiento ? 'Sí' : 'No';
	//const tieneVencimiento = producto.tieneVencimiento === true ? 'Sí' : 'No';
	const item = document.createElement('div');
	    item.className = 'list-item';

	    item.innerHTML = `
	      <div>
	        <strong>${producto.codigo}</strong> - ${producto.descripcion}<br>
	        <small>Fecha Inicio: ${formatFecha(producto.fechaInicio)} | Fecha Fin: ${formatFecha(producto.fechaFin)}</small><br>
	        <small>Tiene Vencimiento: ${tieneVencimiento}</small><br>
	        <small>Tipo: ${producto.tipoProducto.descripcion}</small><br>
	        <small>Stock Máximo: ${producto.stockMaximo} | Stock Mínimo: ${producto.stockMinimo} | Punto Reposición: ${producto.puntoReposicion}</small>
	      </div>
	      <button onclick="deleteProducto(${producto.id})">Eliminar</button>
	      <a href="modificar-producto.html?id=${producto.id}"><button>Modificar</button></a>
	    `;
	    list.appendChild(item);
	  });
	}

// Formatear fecha como YYYY-MM-DD
function formatFecha(fecha) {
  return new Date(fecha).toISOString().split('T')[0];
}

// Eliminar un producto
async function deleteProducto(id) {
  if (confirm('¿Está seguro de que desea eliminar este producto?')) {
    try {
      const response = await fetch(`${API_BASE_URL}/productos/${id}`, { method: 'DELETE' });
      if (response.ok) {
        alert('Producto eliminado exitosamente.');
        fetchProductos();
      }
    } catch (error) {
      console.error('Error al eliminar producto:', error);
    }
  }
}

// Obtener tipos de producto y llenar el <select>
async function fetchTiposProducto() {
  try {
    const response = await fetch(`${API_BASE_URL}/tipos-producto`);
	if (!response.ok) {
	      throw new Error(`Error: ${response.status}`);
	    }
	    const tipos = await response.json();
	    renderTiposProducto(Array.isArray(tipos) ? tipos : []);
	  } catch (error) {
	    console.error('Error al cargar tipos de productos:', error);
	  }
}


// Rellenar el <select> con los tipos de productos
function renderTiposProducto(tipos) {
  const tipoSelect = document.getElementById('producto-tipo');
  tipoSelect.innerHTML = ''; // Limpiar el select

  // Añadir la opción por defecto
  const defaultOption = document.createElement('option');
    defaultOption.value = '';
    defaultOption.textContent = 'Seleccione un tipo de producto';
    defaultOption.disabled = true;
    defaultOption.selected = true;
    tipoSelect.appendChild(defaultOption);
	
	if (!tipos || tipos.length === 0) {
	   console.warn('No hay tipos de productos disponibles.');
	   return; // No hay nada más que hacer
	 }

  // Añadir las opciones de los tipos de producto
  tipos.forEach((tipo) => {
      const option = document.createElement('option');
      option.value = tipo.id;
      option.textContent = tipo.descripcion;
      tipoSelect.appendChild(option);
    });
}