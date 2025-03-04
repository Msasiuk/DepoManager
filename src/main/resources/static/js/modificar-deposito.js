const API_BASE_URL = 'http://localhost:8080/api';
const depositoId = new URLSearchParams(window.location.search).get('id'); // Obtener ID del depósito desde la URL

document.addEventListener('DOMContentLoaded', () => {
	cargarDeposito(depositoId);
	
	document.getElementById('modificar-deposito-form').addEventListener('submit', async (event) => {
		event.preventDefault();
		await guardarCambios();
	});
});

// Redirigir a la página principal al hacer clic en "Volver"
function volver() {
	window.location.href = 'depositos.html';
}

// Cargar datos del depósito en el formulario
async function cargarDeposito(id) {
	try {
		const response = await fetch(`${API_BASE_URL}/depositos/${id}`);
		if (!response.ok) throw new Error('Depósito no encontrado');
		const deposito = await response.json();
		
		document.getElementById('deposito-codigo').value = deposito.codigo;
		document.getElementById('deposito-descripcion').value = deposito.descripcion;
		document.getElementById('deposito-fecha-inicio').value = formatFecha(deposito.fechaInicio);
		document.getElementById('deposito-fecha-fin').value = formatFecha(deposito.fechaFin);
	} catch (error) {
		console.error('Error al cargar el depósito:', error);
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

// Guardar los cambios en el depósito
async function guardarCambios() {
	const fechaInicio = document.getElementById('deposito-fecha-inicio').value;
	const fechaFin = document.getElementById('deposito-fecha-fin').value;
	
	// Validación en el frontend: Fecha fin no puede ser menor que fecha inicio
	if (new Date(fechaFin) < new Date(fechaInicio)) {
		alert('La fecha de finalización no puede ser anterior a la fecha de inicio.');
		return; // Detener el envío
	}
	
	const deposito = {
		id: depositoId,
		codigo: document.getElementById('deposito-codigo').value,
		descripcion: document.getElementById('deposito-descripcion').value,
		fechaInicio: formatFecha(fechaInicio),
		fechaFin: formatFecha(fechaFin),
	};
	
	try {
		const response = await fetch(`${API_BASE_URL}/depositos/${deposito.id}`, {
			method: 'PUT',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(deposito)
		});
		
		if (response.ok) {
			alert('Depósito modificado exitosamente.');
			window.location.href = 'depositos.html';
		} else {
			const errorText = await response.text();
			alert(`Error: ${errorText}`);
		}
	} catch (error) {
		console.error('Error al guardar los cambios:', error);
		alert('Error al guardar los cambios.');
	}
}