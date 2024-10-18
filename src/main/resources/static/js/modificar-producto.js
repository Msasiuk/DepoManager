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
    const producto = await response.json();

    document.getElementById('producto-id').value = producto.id;
    document.getElementById('producto-codigo').value = producto.codigo;
    document.getElementById('producto-descripcion').value = producto.descripcion;
    document.getElementById('producto-fecha-inicio').value = formatFecha(producto.fechaInicio);
    document.getElementById('producto-fecha-fin').value = formatFecha(producto.fechaFin);
    document.getElementById('producto-tipo').value = producto.tipoProducto.id;
    document.getElementById('producto-cantidad').value = producto.cantidad;
    document.getElementById('producto-stock-max').value = producto.stockMaximo;
    document.getElementById('producto-stock-min').value = producto.stockMinimo;
  } catch (error) {
    console.error('Error al cargar el producto:', error);
  }
}

// Formatear fecha en formato YYYY-MM-DD
function formatFecha(fecha) {
  return new Date(fecha).toISOString().split('T')[0];
}

// Obtener y llenar los tipos de producto en el <select>
async function fetchTiposProducto() {
  try {
    const response = await fetch(`${API_BASE_URL}/tipo-productos`);
    const tipos = await response.json();
    renderTiposProducto(tipos);
  } catch (error) {
    console.error('Error al cargar tipos de productos:', error);
  }
}

function renderTiposProducto(tipos) {
  const tipoSelect = document.getElementById('producto-tipo');
  tipoSelect.innerHTML = '';

  tipos.forEach(tipo => {
    const option = document.createElement('option');
    option.value = tipo.id;
    option.textContent = tipo.descripcion;
    tipoSelect.appendChild(option);
  });
}

// Guardar los cambios en el producto
async function guardarCambios() {
	// Validación de valores positivos
		    const cantidad = parseInt(document.getElementById('producto-cantidad').value) || 0;
		    const stockMaximo = parseInt(document.getElementById('producto-stock-max').value) || 0;
		    const stockMinimo = parseInt(document.getElementById('producto-stock-min').value) || 0;

		    if (cantidad < 0 || stockMaximo < 0 || stockMinimo < 0) {
		      alert('La cantidad y los stocks deben ser valores positivos.');
		      return;
		    }
  const producto = {
    id: document.getElementById('producto-id').value,
    codigo: document.getElementById('producto-codigo').value,
    descripcion: document.getElementById('producto-descripcion').value,
    fechaInicio: document.getElementById('producto-fecha-inicio').value,
    fechaFin: document.getElementById('producto-fecha-fin').value,
    tipoProducto: { id: document.getElementById('producto-tipo').value },
    cantidad: parseInt(document.getElementById('producto-cantidad').value) || 0,
    stockMaximo: parseInt(document.getElementById('producto-stock-max').value) || 0,
    stockMinimo: parseInt(document.getElementById('producto-stock-min').value) || 0,
  };

  try {
    const response = await fetch(`${API_BASE_URL}/productos/${producto.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(producto),
    });

    if (response.ok) {
      alert('Producto modificado exitosamente.');
      window.location.href = 'productos.html'; // Redirigir a la lista de productos
    } else {
      const errorText = await response.text();
      alert(`Error: ${errorText}`);
    }
  } catch (error) {
    console.error('Error al guardar los cambios:', error);
    alert('Error al guardar los cambios.');
  }
}
