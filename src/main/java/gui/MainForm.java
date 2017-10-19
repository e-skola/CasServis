package gui;

import dao.KvizDAO;
import dao.MaterijalDAO;
import dao.PitanjeDAO;
import dao.TakmicarDAO;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.ws.Endpoint;
import modeli.Kviz;
import modeli.Materijal;
import modeli.Pitanje;
import modeli.Takmicar;
import org.eclipse.persistence.tools.file.FileUtil;
import servis.CasServis;
import utils.Konverter;

/**
 *
 * @author zi
 */

public class MainForm extends javax.swing.JFrame {
	
	private CasServis casServis;
	
	private MaterijalDAO materijalDAO;
	private KvizDAO kvizDAO;
	private PitanjeDAO pitanjeDAO;
	private TakmicarDAO takmicarDAO;
	
	private List<Materijal> materijali;
	private List<Kviz> kvizovi;
	private List<Pitanje> pitanja;
	private List<Takmicar> rangLista;
	
	private DefaultTableModel modelMaterijali;
	private DefaultTableModel modelPitanje;
	private DefaultTableModel modelRangLista;
	
	Timer tajmerRangLista;
	
	/**
	 * Creates new form MainForm
	 */
	public MainForm() {
		casServis = new CasServis();
		
		materijalDAO = new MaterijalDAO();
		kvizDAO = new KvizDAO();
		pitanjeDAO = new PitanjeDAO();
		
		initModels();
		initComponents();
		osvezi();
		osveziKvizove();
		osveziKviz();
		podesiTajmere();
	}
	
	public void osvezi() {
		int razred = (int)spinnerRazred.getValue();
		int lekcija = (int)spinnerLekcija.getValue();
		
		materijali = materijalDAO.preuzmi(razred, lekcija);
		
		while(modelMaterijali.getRowCount() > 0) {
			modelMaterijali.removeRow(0);
		}
		
		for(Materijal materijal : materijali) {
			modelMaterijali.addRow(new Object[] {
				materijal.getId(),
				materijal.getRazred(),
				materijal.getLekcija()
			});
		}
		
		lblSlika.setIcon(new ImageIcon());
	}
	
	public void osveziKvizove() {
		int selektovaniKviz = cbNazivKviza.getSelectedIndex();
		if(selektovaniKviz < 0)
			selektovaniKviz = 0;
		
		kvizovi = kvizDAO.preuzmiSve();
		cbNazivKviza.removeAllItems();
		for(Kviz kviz : kvizovi) {
			cbNazivKviza.addItem(kviz.getNaziv());
		}
		
		cbNazivKviza.setSelectedIndex(selektovaniKviz);
		selektovaniKviz = cbNazivKviza.getSelectedIndex();
		if(selektovaniKviz >= 0) {
			pitanja = pitanjeDAO.preuzmi(kvizovi.get(selektovaniKviz));
			while(modelPitanje.getRowCount() > 0) {
				modelPitanje.removeRow(0);
			}
			for(Pitanje pitanje : pitanja) {
				modelPitanje.addRow(new Object[] {
					pitanje.getId(),
					(char)(pitanje.getTacanOdgovor() - 1 + 'А')
				});
			}
		}
		
		lblPitanjeSlika.setIcon(new ImageIcon());
	}
	
	public void osveziKviz() {
		int selektovaniKviz = cbKviz.getSelectedIndex();
		if(selektovaniKviz < 0)
			selektovaniKviz = 0;
		
		kvizovi = kvizDAO.preuzmiSve();
		cbKviz.removeAllItems();
		for(Kviz kviz : kvizovi) {
			cbKviz.addItem(kviz.getNaziv());
		}
		
		cbKviz.setSelectedIndex(selektovaniKviz);
		selektovaniKviz = cbKviz.getSelectedIndex();
	}
	
	public void osveziRangListu() {
		rangLista = casServis.preuzmiRangListu();
		
		while(modelRangLista.getRowCount() > 0) {
			modelRangLista.removeRow(0);
		}
		
		for(Takmicar takmicar : rangLista) {
			modelRangLista.addRow(new Object[] {
				takmicar.getIme(),
				takmicar.getPrezime(),
				takmicar.getBodovi()
			});
		}
	}
	
	private void podesiTajmere() {
		tajmerRangLista = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						osveziRangListu();
					}
				});
			}
		});
		tajmerRangLista.setRepeats(true);
	}
	
	private void initModels() {
		modelMaterijali = new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"ИД",
				"Разред",
				"Лекција"
			}
		) {
			@Override
			public boolean isCellEditable(int i, int i1) {
				return false;
			}
			
		};
		
		modelPitanje = new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"ИД",
				"Тачан одговор"
			}
		) {
			@Override
			public boolean isCellEditable(int i, int i1) {
				return false;
			}
			
		};
		
		modelRangLista = new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"Име",
				"Презиме",
				"Бодови"
			}
		) {
			@Override
			public boolean isCellEditable(int i, int i1) {
				return false;
			}
			
		};
	}
	
	public void prikaziSliku(Materijal materijal) {
		lblSlika.setIcon(new ImageIcon());
		try {
			materijal.ucitajSliku();
			BufferedImage slika = Konverter.byteArrayToImage(materijal.getSlika());
			Image sSlika = slika.getScaledInstance(lblSlika.getWidth(), lblSlika.getHeight(), Image.SCALE_SMOOTH);
			lblSlika.setIcon(new ImageIcon(sSlika));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void prikaziSlikuPitanja(Pitanje pitanje) {
		lblPitanjeSlika.setIcon(new ImageIcon());
		try {
			pitanje.ucitajSliku();
			BufferedImage slika = Konverter.byteArrayToImage(pitanje.getSlika());
			Image sSlika = slika.getScaledInstance(lblPitanjeSlika.getWidth(), lblPitanjeSlika.getHeight(), Image.SCALE_SMOOTH);
			lblPitanjeSlika.setIcon(new ImageIcon(sSlika));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tpGlavni = new javax.swing.JTabbedPane();
        panelServis = new javax.swing.JPanel();
        btnPokreni = new javax.swing.JButton();
        panelKviz = new javax.swing.JPanel();
        cbKviz = new javax.swing.JComboBox<>();
        lblKviz = new javax.swing.JLabel();
        btnStartStop = new javax.swing.JButton();
        lblRangLista = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblRangLista = new javax.swing.JTable();
        panelMaterijali = new javax.swing.JPanel();
        lblRazred = new javax.swing.JLabel();
        spinnerRazred = new javax.swing.JSpinner();
        lblLekcija = new javax.swing.JLabel();
        spinnerLekcija = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMaterijali = new javax.swing.JTable();
        lblSlika = new javax.swing.JLabel();
        btnNoviRed = new javax.swing.JButton();
        btnUcitajSliku = new javax.swing.JButton();
        btnObrisi = new javax.swing.JButton();
        panelKvizovi = new javax.swing.JPanel();
        lblNazivKviza = new javax.swing.JLabel();
        cbNazivKviza = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPitanja = new javax.swing.JTable();
        lblPitanjeSlika = new javax.swing.JLabel();
        btnUcitajSlikuPitanja = new javax.swing.JButton();
        btnDodajPitanje = new javax.swing.JButton();
        btnObrisiPitanje = new javax.swing.JButton();
        btnDodajKviz = new javax.swing.JButton();
        btnObrisiKviz = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ЧАС");

        btnPokreni.setText("ПОКРЕНИ");
        btnPokreni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPokreniActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelServisLayout = new javax.swing.GroupLayout(panelServis);
        panelServis.setLayout(panelServisLayout);
        panelServisLayout.setHorizontalGroup(
            panelServisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServisLayout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addComponent(btnPokreni)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        panelServisLayout.setVerticalGroup(
            panelServisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServisLayout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addComponent(btnPokreni)
                .addContainerGap(352, Short.MAX_VALUE))
        );

        tpGlavni.addTab("Сервис", panelServis);

        lblKviz.setText("Квиз:");

        btnStartStop.setText("Започни");
        btnStartStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartStopActionPerformed(evt);
            }
        });

        lblRangLista.setText("Ранг листа:");

        tblRangLista.setModel(modelRangLista);
        jScrollPane3.setViewportView(tblRangLista);

        javax.swing.GroupLayout panelKvizLayout = new javax.swing.GroupLayout(panelKviz);
        panelKviz.setLayout(panelKvizLayout);
        panelKvizLayout.setHorizontalGroup(
            panelKvizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKvizLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKvizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                    .addGroup(panelKvizLayout.createSequentialGroup()
                        .addGroup(panelKvizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelKvizLayout.createSequentialGroup()
                                .addComponent(lblKviz)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbKviz, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnStartStop))
                            .addComponent(lblRangLista))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelKvizLayout.setVerticalGroup(
            panelKvizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKvizLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKvizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbKviz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKviz)
                    .addComponent(btnStartStop))
                .addGap(18, 18, 18)
                .addComponent(lblRangLista)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
        );

        tpGlavni.addTab("Квиз", panelKviz);

        lblRazred.setText("Разред:");

        spinnerRazred.setModel(new javax.swing.SpinnerNumberModel(5, 5, 8, 1));
        spinnerRazred.setToolTipText("");
        spinnerRazred.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerRazredStateChanged(evt);
            }
        });

        lblLekcija.setText("Лекција:");

        spinnerLekcija.setModel(new javax.swing.SpinnerNumberModel(1, 1, 99, 1));
        spinnerLekcija.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerLekcijaStateChanged(evt);
            }
        });

        tblMaterijali.setModel(modelMaterijali);
        tblMaterijali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMaterijaliMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblMaterijali);

        lblSlika.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSlikaMouseClicked(evt);
            }
        });

        btnNoviRed.setText("Додај");
        btnNoviRed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoviRedActionPerformed(evt);
            }
        });

        btnUcitajSliku.setText("Учитај слику ...");
        btnUcitajSliku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUcitajSlikuActionPerformed(evt);
            }
        });

        btnObrisi.setText("Обриши");
        btnObrisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObrisiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMaterijaliLayout = new javax.swing.GroupLayout(panelMaterijali);
        panelMaterijali.setLayout(panelMaterijaliLayout);
        panelMaterijaliLayout.setHorizontalGroup(
            panelMaterijaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMaterijaliLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMaterijaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMaterijaliLayout.createSequentialGroup()
                        .addComponent(lblRazred)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerRazred, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblLekcija)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerLekcija, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelMaterijaliLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMaterijaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMaterijaliLayout.createSequentialGroup()
                                .addGroup(panelMaterijaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnUcitajSliku)
                                    .addGroup(panelMaterijaliLayout.createSequentialGroup()
                                        .addComponent(btnNoviRed)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnObrisi)))
                                .addGap(0, 48, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMaterijaliLayout.createSequentialGroup()
                                .addComponent(lblSlika, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))))
        );
        panelMaterijaliLayout.setVerticalGroup(
            panelMaterijaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMaterijaliLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMaterijaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRazred)
                    .addComponent(spinnerRazred, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLekcija)
                    .addComponent(spinnerLekcija, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMaterijaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                    .addGroup(panelMaterijaliLayout.createSequentialGroup()
                        .addComponent(lblSlika, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUcitajSliku)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelMaterijaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNoviRed)
                            .addComponent(btnObrisi))))
                .addContainerGap())
        );

        tpGlavni.addTab("Материјали за час", panelMaterijali);

        lblNazivKviza.setText("Назив квиза:");

        cbNazivKviza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNazivKvizaActionPerformed(evt);
            }
        });

        tblPitanja.setModel(modelPitanje);
        tblPitanja.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPitanjaMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblPitanja);

        lblPitanjeSlika.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPitanjeSlikaMouseClicked(evt);
            }
        });

        btnUcitajSlikuPitanja.setText("Учитај слику ...");
        btnUcitajSlikuPitanja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUcitajSlikuPitanjaActionPerformed(evt);
            }
        });

        btnDodajPitanje.setText("Додај");
        btnDodajPitanje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajPitanjeActionPerformed(evt);
            }
        });

        btnObrisiPitanje.setText("Обриши");
        btnObrisiPitanje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObrisiPitanjeActionPerformed(evt);
            }
        });

        btnDodajKviz.setText("Додај квиз");
        btnDodajKviz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajKvizActionPerformed(evt);
            }
        });

        btnObrisiKviz.setText("Обриши квиз");
        btnObrisiKviz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObrisiKvizActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelKvizoviLayout = new javax.swing.GroupLayout(panelKvizovi);
        panelKvizovi.setLayout(panelKvizoviLayout);
        panelKvizoviLayout.setHorizontalGroup(
            panelKvizoviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKvizoviLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKvizoviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKvizoviLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelKvizoviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPitanjeSlika, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                            .addGroup(panelKvizoviLayout.createSequentialGroup()
                                .addGroup(panelKvizoviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnUcitajSlikuPitanja)
                                    .addGroup(panelKvizoviLayout.createSequentialGroup()
                                        .addComponent(btnDodajPitanje)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnObrisiPitanje)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(panelKvizoviLayout.createSequentialGroup()
                        .addComponent(lblNazivKviza)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbNazivKviza, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDodajKviz)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnObrisiKviz)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelKvizoviLayout.setVerticalGroup(
            panelKvizoviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKvizoviLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKvizoviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNazivKviza)
                    .addComponent(cbNazivKviza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDodajKviz)
                    .addComponent(btnObrisiKviz))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKvizoviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                    .addGroup(panelKvizoviLayout.createSequentialGroup()
                        .addComponent(lblPitanjeSlika, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUcitajSlikuPitanja)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelKvizoviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnDodajPitanje)
                            .addComponent(btnObrisiPitanje))))
                .addContainerGap())
        );

        tpGlavni.addTab("Квизови", panelKvizovi);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tpGlavni)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tpGlavni)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void spinnerRazredStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerRazredStateChanged
        osvezi();
    }//GEN-LAST:event_spinnerRazredStateChanged

    private void spinnerLekcijaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerLekcijaStateChanged
        osvezi();
    }//GEN-LAST:event_spinnerLekcijaStateChanged

    private void btnNoviRedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoviRedActionPerformed
        Materijal materijal = new Materijal();
		materijal.setRazred((int)spinnerRazred.getValue());
		materijal.setLekcija((int)spinnerLekcija.getValue());
		materijalDAO.dodaj(materijal);
		osvezi();
    }//GEN-LAST:event_btnNoviRedActionPerformed

    private void tblMaterijaliMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMaterijaliMousePressed
        int row = tblMaterijali.rowAtPoint(evt.getPoint());
		Materijal materijal = materijali.get(row);
		prikaziSliku(materijal);
    }//GEN-LAST:event_tblMaterijaliMousePressed

    private void lblSlikaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSlikaMouseClicked
        JFrame frmSlika = new JFrame();
		int row = tblMaterijali.getSelectedRow();
		Materijal materijal = materijali.get(row);
		
		frmSlika.setTitle(String.valueOf(materijal.getId()));
		try {
			BufferedImage slika = Konverter.byteArrayToImage(materijal.getSlika());
			frmSlika.add(new JLabel(new ImageIcon(slika)));
			frmSlika.setVisible(true);
			frmSlika.pack();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
    }//GEN-LAST:event_lblSlikaMouseClicked

    private void btnObrisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObrisiActionPerformed
        int row = tblMaterijali.getSelectedRow();
		if(row >= 0) {
			Materijal materijal = materijali.get(row);
			if(materijalDAO.obrisi(materijal)) {
				File slika = new File("res/materijal/" + materijal.getId() + ".jpg");
				slika.delete();
				osvezi();
			}
		}
    }//GEN-LAST:event_btnObrisiActionPerformed

    private void btnPokreniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPokreniActionPerformed
        new Thread(new Runnable() {
			@Override
			public void run() {
				Endpoint.publish("http://0.0.0.0:5001/Cas", casServis);
			}
		}).start();
    }//GEN-LAST:event_btnPokreniActionPerformed

    private void btnUcitajSlikuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUcitajSlikuActionPerformed
        int row = tblMaterijali.getSelectedRow();
		if(row < 0)
			return;
		Materijal materijal = materijali.get(row);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Slika", "jpg"));
		if(fileChooser.showOpenDialog(MainForm.this) == JFileChooser.APPROVE_OPTION) {
			try {
				File inFile = fileChooser.getSelectedFile();
				File outFile = new File("res/materijal/" + materijal.getId() + ".jpg");
				outFile.delete();
				outFile.createNewFile();
				FileUtil.copy(new FileInputStream(inFile), new FileOutputStream(outFile));
				prikaziSliku(materijal);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
    }//GEN-LAST:event_btnUcitajSlikuActionPerformed

    private void lblPitanjeSlikaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPitanjeSlikaMouseClicked
        JFrame frmSlika = new JFrame();
		int row = tblPitanja.getSelectedRow();
		Pitanje pitanje = pitanja.get(row);
		
		frmSlika.setTitle(String.valueOf(pitanje.getId()));
		try {
			BufferedImage slika = Konverter.byteArrayToImage(pitanje.getSlika());
			frmSlika.add(new JLabel(new ImageIcon(slika)));
			frmSlika.setVisible(true);
			frmSlika.pack();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
    }//GEN-LAST:event_lblPitanjeSlikaMouseClicked

    private void tblPitanjaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPitanjaMousePressed
        int row = tblPitanja.rowAtPoint(evt.getPoint());
		Pitanje pitanje = pitanja.get(row);
		prikaziSlikuPitanja(pitanje);
    }//GEN-LAST:event_tblPitanjaMousePressed

    private void btnDodajPitanjeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajPitanjeActionPerformed
        int row = cbNazivKviza.getSelectedIndex();
		if(row < 0)
			return;
		
		int kvizId = kvizovi.get(row).getId();
		NovoPitanjeDialog dlgNovoPitanje = new NovoPitanjeDialog(this, true, kvizId);
		dlgNovoPitanje.setVisible(true);
    }//GEN-LAST:event_btnDodajPitanjeActionPerformed

    private void btnDodajKvizActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajKvizActionPerformed
        NoviKvizDialog dlgNoviKviz = new NoviKvizDialog(this, true);
		dlgNoviKviz.setVisible(true);
    }//GEN-LAST:event_btnDodajKvizActionPerformed

    private void cbNazivKvizaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNazivKvizaActionPerformed
        osveziKvizove();
    }//GEN-LAST:event_cbNazivKvizaActionPerformed

    private void btnObrisiKvizActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObrisiKvizActionPerformed
        int row = cbNazivKviza.getSelectedIndex();
		if(row < 0)
			return;
		
		Kviz kviz = kvizovi.get(row);
		if(kvizDAO.obrisi(kviz)) {
			List<Pitanje> pitanja = pitanjeDAO.preuzmi(kviz);
			for(Pitanje pitanje : pitanja) {
				if(pitanjeDAO.obrisi(pitanje)) {
					File slika = new File("res/kviz/" + pitanje.getId() + ".jpg");
					slika.delete();
				}
			}
		}
		
		osveziKvizove();
    }//GEN-LAST:event_btnObrisiKvizActionPerformed

    private void btnUcitajSlikuPitanjaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUcitajSlikuPitanjaActionPerformed
        int row = tblPitanja.getSelectedRow();
		if(row < 0)
			return;
		Pitanje pitanje = pitanja.get(row);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Slika", "jpg"));
		if(fileChooser.showSaveDialog(MainForm.this) == JFileChooser.APPROVE_OPTION) {
			try {
				File inFile = fileChooser.getSelectedFile();
				File outFile = new File("res/kviz/" + pitanje.getId() + ".jpg");
				outFile.delete();
				outFile.createNewFile();
				FileUtil.copy(new FileInputStream(inFile), new FileOutputStream(outFile));
				prikaziSlikuPitanja(pitanje);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
    }//GEN-LAST:event_btnUcitajSlikuPitanjaActionPerformed

    private void btnObrisiPitanjeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObrisiPitanjeActionPerformed
        int row = tblPitanja.getSelectedRow();
		if(row >= 0) {
			Pitanje pitanje = pitanja.get(row);
			if(pitanjeDAO.obrisi(pitanje)) {
				File slika = new File("res/kviz/" + pitanje.getId() + ".jpg");
				slika.delete();
				osveziKvizove();
			}
		}
    }//GEN-LAST:event_btnObrisiPitanjeActionPerformed

    private void btnStartStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartStopActionPerformed
        if(!casServis.isKvizPokrenut()) {
			int row = cbKviz.getSelectedIndex();
			if(row < 0)
				return;
			try {
				casServis.pokreniKviz(kvizovi.get(row));
				btnStartStop.setText("Заврши");
				tajmerRangLista.start();
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "Грешка приликом учитавања ресурса квиза");
			}
		} else {
			casServis.zaustaviKviz();
			btnStartStop.setText("Започни");
			tajmerRangLista.stop();
		}
    }//GEN-LAST:event_btnStartStopActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			com.jtattoo.plaf.mint.MintLookAndFeel.setTheme("Mint");
			UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainForm().setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDodajKviz;
    private javax.swing.JButton btnDodajPitanje;
    private javax.swing.JButton btnNoviRed;
    private javax.swing.JButton btnObrisi;
    private javax.swing.JButton btnObrisiKviz;
    private javax.swing.JButton btnObrisiPitanje;
    private javax.swing.JButton btnPokreni;
    private javax.swing.JButton btnStartStop;
    private javax.swing.JButton btnUcitajSliku;
    private javax.swing.JButton btnUcitajSlikuPitanja;
    private javax.swing.JComboBox<String> cbKviz;
    private javax.swing.JComboBox<String> cbNazivKviza;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblKviz;
    private javax.swing.JLabel lblLekcija;
    private javax.swing.JLabel lblNazivKviza;
    private javax.swing.JLabel lblPitanjeSlika;
    private javax.swing.JLabel lblRangLista;
    private javax.swing.JLabel lblRazred;
    private javax.swing.JLabel lblSlika;
    private javax.swing.JPanel panelKviz;
    private javax.swing.JPanel panelKvizovi;
    private javax.swing.JPanel panelMaterijali;
    private javax.swing.JPanel panelServis;
    private javax.swing.JSpinner spinnerLekcija;
    private javax.swing.JSpinner spinnerRazred;
    private javax.swing.JTable tblMaterijali;
    private javax.swing.JTable tblPitanja;
    private javax.swing.JTable tblRangLista;
    private javax.swing.JTabbedPane tpGlavni;
    // End of variables declaration//GEN-END:variables
}
