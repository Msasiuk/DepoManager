const API_BASE_URL = 'http://localhost:8080/api';
const productoId = new URLSearchParams(window.location.search).get('id'); // Obtener ID del producto desde la URL

document.addEventListener('DOMContentLoaded', () => {
  cargarProducto(productoId);
  fetchTiposProducto();

 document.getElementById('modificar-producto-form').addEventListener('submit', async (event) => {
    event.preventDefault();
    await guardarCambios();
  });
});

// Redirigir a la página principal al hacer clic en "Volver"
function volver() {
  window.location.href = 'productos.html';
}

// Cargar datos del producto en el formulario
async function cargarProducto(id) {
  try {
    const response = await fetch(`${API_BASE_URL}/productos/${id}`);
    if (!response.ok) throw new Error('Producto no encontrado');
    
    const producto = await response.json();

    document.getElementById('producto-codigo').value = producto.codigo;
    document.getElementById('producto-descripcion').value = producto.descripcion;
    document.getElementById('producto-fecha-inicio').value = formatFecha(producto.fechaInicio);
    document.getElementById('producto-fecha-fin').value = formatFecha(producto.fechaFin);
    document.getElementById('producto-tiene-vencimiento').value = producto.tieneVencimiento.toString();
    document.getElementById('producto-punto-reposicion').value = producto.puntoReposicion;
    document.getElementById('producto-stock-max').value = producto.stockMaximo;
    document.getElementById('producto-stock-min').value = producto.stockMinimo;

    // Esperar a que se carguen los tipos de producto y luego seleccionar el valor adecuado
    await fetchTiposProducto();
    document.getElementById('producto-tipo').value = producto.tipoProducto.id;

  } catch (error) {
    console.error('Error al cargar el producto:', error);
  }
}

// Formatear fecha en formato YYYY-MM-DD
function formatFecha(fecha) {
  return new Date(fecha).toISOString().split('T')[0];
}

// Guardar los cambios en el producto
async function guardarCambios() {
	// Validación de valores positivos
		const stockMaximo = parseInt(document.getElementById('producto-stock-max').value) || 0;
		const stockMinimo = parseInt(document.getElementById('producto-stock-min').value) || 0;
		const puntoReposicion = parseInt(document.getElementById('producto-punto-reposicion').value )|| 0;

		if (stockMaximo < 0 || stockMinimo < 0 || puntoReposicion < 0) {
		      alert('Los stocks y el punto de reposición deben ser valores positivos.');
		      return;
		}
		
  const producto = {
    id: productoId, // Aquí es obligatorio para guardar los cambios
    codigo: document.getElementById('producto-codigo').value,
    descripcion: document.getElementById('producto-descripcion').value,
	puntoReposicion: puntoReposicion,
	stockMaximo: stockMaximo,
    stockMinimo: stockMinimo,
	tipoProducto: { id: parseInt(document.getElementById('producto-tipo').value) },
	tieneVencimiento: document.getElementById('producto-tiene-vencimiento').value === 'true',
	fechaFin: document.getElementById('producto-fecha-fin').value,
	};

  try {
    const response = await fetch(`${API_BASE_URL}/productos/${producto.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(producto),
    });

    if (response.ok) {
      alert('Producto modificado exitosamente.');
      window.location.href = 'productos.html';
    } else {
      const errorText = await response.text();
      alert(`Error: ${errorText}`);
    }
  } catch (error) {
    console.error('Error al guardar los cambios:', error);
    alert('Error al guardar los cambios.');
  }
}

// Obtener tipos de producto y llenar el <select>
async function fetchTiposProducto() {
  try {
    const response = await fetch(`${API_BASE_URL}/tipos-producto`);
    if (!response.ok) throw new Error(`Error: ${response.status}`);
    
    const tipos = await response.json();
    renderTiposProducto(tipos);
  } catch (error) {
    console.error('Error al cargar tipos de productos:', error);
  }
}

// Rellenar el <select> con los tipos de productos
function renderTiposProducto(tipos) {
  const tipoSelect = document.getElementById('producto-tipo');
  tipoSelect.innerHTML = ''; // Limpiar el select

  const defaultOption = document.createElement('option');
  defaultOption.value = '';
  defaultOption.textContent = 'Seleccione un tipo de producto';
  defaultOption.disabled = true;
  defaultOption.selected = true;
  tipoSelect.appendChild(defaultOption);

  tipos.forEach((tipo) => {
    const option = document.createElement('option');
    option.value = tipo.id;
    option.textContent = tipo.descripcion;
    tipoSelect.appendChild(option);
  });
}