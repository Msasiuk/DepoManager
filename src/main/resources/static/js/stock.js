const API_BASE_URL = 'http://localhost:8080/api';

// Esperar a que el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
	fetchDepositos(); // Cargar los depósitos al inicio
	
	// Asignar evento al botón Ver Stock
	const verStockBtn = document.getElementById('ver-stock-btn');
	verStockBtn.addEventListener('click', () => {
		const depositoId = document.getElementById('deposito-id').value;
		if (!depositoId) {
			alert('Por favor, seleccione un depósito.');
			return;
		}
		fetchStockPorDeposito(depositoId); // Llamar a la función para obtener el stock
	});
});

// Obtener y mostrar los depósitos en el <select>
async function fetchDepositos() {
	try {
		const response = await fetch(`${API_BASE_URL}/depositos`);
		if (!response.ok) {
			throw new Error(`Error: ${response.status}`);
		}
		
		const depositos = await response.json();
		console.log('Depósitos recibidos:', depositos); // Verificar en la consola
		renderSelectOptions('deposito-id', depositos);
	} catch (error) {
		console.error('Error al cargar depósitos:', error);
	}
}

// Rellenar el <select> con los depósitos disponibles
function renderSelectOptions(selectId, items) {
	const select = document.getElementById(selectId);
	select.innerHTML = ''; // Limpiar opciones previas
	
	items.forEach(item => {
		const option = document.createElement('option');
		option.value = item.id;
		option.textContent = item.descripcion || 'Sin descripción'; // Usar la propiedad 'descripcion'
		select.appendChild(option);
	});
}

// Consultar el stock disponible en un depósito específico
async function fetchStockPorDeposito(depositoId) {
	try {
		const response = await fetch(`${API_BASE_URL}/stock/deposito/${depositoId}`);
		if (!response.ok) {
			throw new Error(`Error: ${response.status}`);
		}
		
		const stockItems = await response.json();
		console.log('Stock recibido:', stockItems);
		
		renderStockList(stockItems);
	} catch (error) {
		console.error('Error al cargar stock:', error);
	}
}

// Renderizar la lista de stock
function renderStockList(stockItems) {
	const list = document.getElementById('stock-list');
	list.innerHTML = '';
	
	if (stockItems.length === 0) {
		list.innerHTML = '<p>No hay stock disponible en este depósito.</p>';
		return;
	}
	
	stockItems.forEach(stock => {
		const item = document.createElement('div');
		item.className = 'list-item';
		
		item.innerHTML = `<div>
			<strong>Producto:</strong> ${stock.producto.descripcion} <br>
			<strong>Cantidad:</strong> ${stock.cantidad} <br>
			<strong>Fecha Vencimiento:</strong> ${stock.fechaVencimiento || 'N/A'}
			</div>`;
			
		list.appendChild(item);
	});
}