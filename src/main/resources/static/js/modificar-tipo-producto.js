const API_BASE_URL = 'http://localhost:8080/api';
const tipoProductoId = new URLSearchParams(window.location.search).get('id'); // Obtener ID del producto desde la URL

document.addEventListener('DOMContentLoaded', () => {
  cargarTipoProducto(tipoProductoId);

  document.getElementById('modificar-tipo-form').addEventListener('submit', async (event) => {
    event.preventDefault();
    await guardarCambios();
  });
});

// Redirigir a la página principal al hacer clic en "Volver"
function volver() {
  window.location.href = 'tipos-producto.html';
}

// Cargar datos del producto en el formulario
async function cargarTipoProducto(id) {
	
  try {
    const response = await fetch(`${API_BASE_URL}/tipos-producto/${id}`);
	if (!response.ok) throw new Error('Tipo producto no encontrado');
    const tipoProducto = await response.json();

    document.getElementById('tipo-codigo').value = tipoProducto.codigo;
    document.getElementById('tipo-descripcion').value = tipoProducto.descripcion;
    document.getElementById('tipo-fecha-inicio').value = formatFecha(tipoProducto.fechaInicio);
    document.getElementById('tipo-fecha-fin').value = formatFecha(tipoProducto.fechaFin);
  } catch (error) {
    console.error('Error al cargar el tipo producto:', error);
  }
}

// Formatear fecha en formato YYYY-MM-DD
function formatFecha(fecha) {
  return new Date(fecha).toISOString().split('T')[0];
}

// Guardar los cambios en el producto
async function guardarCambios() {
	const tipoProducto = {
     id: tipoProductoId, // Aquí es obligatorio para guardar los cambios
    codigo: document.getElementById('tipo-codigo').value,
    descripcion: document.getElementById('tipo-descripcion').value,
    fechaInicio: document.getElementById('tipo-fecha-inicio').value,
    fechaFin: document.getElementById('tipo-fecha-fin').value,
  };

  try {
    const response = await fetch(`${API_BASE_URL}/tipos-producto/${tipoProducto.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(tipoProducto),
    });

    if (response.ok) {
      alert('Tipo producto modificado exitosamente.');
      window.location.href = 'tipos-producto.html'; // Redirigir a la lista de tipos producto
    } else {
      const errorText = await response.text();
      alert(`Error: ${errorText}`);
    }
  } catch (error) {
    console.error('Error al guardar los cambios:', error);
    alert('Error al guardar los cambios.');
  }
}
