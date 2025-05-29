window.main = async function () {
  async function loadCSV(url) {
    const response = await fetch(url);
    const text = await response.text();
    const parsed = Papa.parse(text, { header: true }).data;

    console.assert(Array.isArray(parsed), `Eroare: Fisierul ${url} nu a fost procesat corect ca array.`);
    return parsed;
  }

  const grupe = await loadCSV('data/grupe.csv');
  console.assert(grupe.length > 0, 'Eroare: Nicio grupa incarcata din grupe.csv.');

  const ani = await loadCSV('data/ani.csv');
  console.assert(ani.length > 0, 'Eroare: Niciun an incarcat din ani.csv.');

  const lista = document.getElementById('listaGrupe');
  console.assert(lista, 'Eroare: Elementul cu id="listaGrupe" nu a fost gasit.');

  grupe.forEach(gr => {
    console.assert('id' in gr && 'an' in gr, `Eroare: Grupa invalida: ${JSON.stringify(gr)}`);

    const an = ani.find(a => a.id === gr.an);
    console.assert(an, `Eroare: Nu a fost gasit anul pentru grupa ${gr.id} (an: ${gr.an})`);

    if (!an) return;

    const li = document.createElement('li');
    const link = document.createElement('a');
    link.href = `orar_grupe.html?grupa=${encodeURIComponent(gr.id)}`;
    link.textContent = `${gr.id} - An ${an.an}, ${an.spec}, ${an.type}`;
    li.appendChild(link);
    lista.appendChild(li);
  });
};
