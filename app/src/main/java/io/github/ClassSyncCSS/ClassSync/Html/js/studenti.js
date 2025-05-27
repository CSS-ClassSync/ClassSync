window.main = async function() {
  async function loadCSV(url) {
    const response = await fetch(url);
    const text = await response.text();
    return Papa.parse(text, { header: true }).data;
  }

  const grupe = await loadCSV('data/grupe.csv');
  const ani = await loadCSV('data/ani.csv');
  const lista = document.getElementById('listaGrupe');
  grupe.forEach(gr => {
    const an = ani.find(a => a.id === gr.an);
    if (!an) return;
    const li = document.createElement('li');
    const link = document.createElement('a');
    link.href = `orar_grupe.html?grupa=${encodeURIComponent(gr.id)}`;
    link.textContent = `${gr.id} - An ${an.an}, ${an.spec}, ${an.type}`;
    li.appendChild(link);
    lista.appendChild(li);
  });
};
