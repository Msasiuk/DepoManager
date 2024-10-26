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
	// Verifica si la fecha es válida antes de formatearla
	if (!fecha) return null;
	const date = new Date(fecha);
	if (isNaN(date)) return null; // Manejo de fechas inválidas
	return date.toISOString().split('T')[0]; // Formato 'YYYY-MM-DD'
}

// Guardar los cambios en el producto
async function guardarCambios() {
	const fechaInicio = document.getElementById('tipo-fecha-inicio').value;
	const fechaFin = document.getElementById('tipo-fecha-fin').value;
		
	// Validación en el frontend: Fecha fin no puede ser menor que fecha inicio
	if (new Date(fechaFin) < new Date(fechaInicio)) {
		alert('La fecha de finalización no puede ser anterior a la fecha de inicio.');
		return; // Detener el envío
	}
		
	const tipoProducto = {
		id: tipoProductoId, // Aquí es obligatorio para guardar los cambios
		codigo: document.getElementById('tipo-codigo').value,
		descripcion: document.getElementById('tipo-descripcion').value,
		fechaInicio: formatFecha(fechaInicio),
		fechaFin: formatFecha(fechaFin),
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
