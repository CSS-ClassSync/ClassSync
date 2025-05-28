describe('sali.js', () => {
  beforeEach(() => {
    document.body.innerHTML = `<ul id="listaSali"></ul>`;

    global.Papa = {
      parse: jest.fn((text, options) => {
        return {
          data: [
            { name: 'Sala 101', tip: 'Curs' },
            { name: 'Laborator 1', tip: 'Lab' }
          ]
        };
      })
    };

    global.fetch = jest.fn(() =>
      Promise.resolve({
        text: () =>
          Promise.resolve(`name,tip\nSala 101,Curs\nLaborator 1,Lab`)
      })
    );
  });

  it('incarca sali.csv și genereaza lista de sali in DOM', async () => {
    require('../js/sali.js');
    await window.main();

    const lista = document.getElementById('listaSali');
    expect(lista.children.length).toBe(2);

    const primul = lista.children[0].querySelector('a');
    expect(primul).not.toBeNull();
    expect(primul.textContent).toBe('Sala 101 - Curs');
    expect(primul.href).toContain('orar_sali.html?sala=Sala%20101');

    const alDoilea = lista.children[1].querySelector('a');
    expect(alDoilea).not.toBeNull();
    expect(alDoilea.textContent).toBe('Laborator 1 - Lab');
    expect(alDoilea.href).toContain('orar_sali.html?sala=Laborator%201');
  });
});
