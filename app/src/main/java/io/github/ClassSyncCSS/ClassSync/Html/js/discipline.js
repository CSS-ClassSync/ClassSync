async function loadCSV(url) {
  const response = await fetch(url);
  const text = await response.text();
  const parsed = Papa.parse(text, { header: true, skipEmptyLines: true }).data;

  console.assert(Array.isArray(parsed), `Eroare: Fisierul ${url} nu a fost procesat corect ca array.`);
  return parsed;
}

async function main() {
  const materii = (await loadCSV('data/materii.csv')).filter(m => m.id && m.name && m.tip);
  console.assert(materii.length > 0, 'Eroare: Nicio materie valida incarcata.');

  const profesoriMaterii = await loadCSV('data/profesori_materii.csv');
  console.assert(profesoriMaterii.length > 0, 'Eroare: profesori_materii.csv este gol sau invalid.');

  const profesori = (await loadCSV('data/profesori.csv')).filter(p => p.id && p.name);
  console.assert(profesori.length > 0, 'Eroare: Niciun profesor valid incarcat.');

  const list = document.getElementById('listaDiscipline');
  console.assert(list, 'Eroare: Elementul cu id="listaDiscipline" nu a fost gasit.');

  materii.forEach(materie => {
    const profIds = profesoriMaterii
      .filter(pm => pm.materie === materie.id)
      .map(pm => pm.profesor);

    console.assert(Array.isArray(profIds), `Eroare: profIds nu este array pentru materia ${materie.id}`);

    const nameProfesori = profIds
      .map(id => {
        const prof = profesori.find(p => p.id === id);
        return prof ? prof.name : null;
      })
      .filter(Boolean);

    if (!materie.name || !materie.tip) return;

    const li = document.createElement('li');
    const link = document.createElement('a');

    link.href = `orar_discipline.html?materie=${encodeURIComponent(materie.name)}`;
    link.textContent = `${materie.name} (${materie.tip}) - ${nameProfesori.length > 0 ? nameProfesori.join(', ') : '(fara profesori)'}`;

    li.appendChild(link);
    list.appendChild(li);
  });
}

window.main = main;
