using System;
using System.Collections.Generic;



using FINT.Model.Administrasjon.Kompleksedatatyper;
using FINT.Model.Felles.Basisklasser;

namespace FINT.Model.Okonomi.Kodeverk
{
	public class Vare : Begrep {
		public enum Relasjonsnavn
        {
			FAKTURAUTSTEDER,
			MERVERDIAVGIFT
        }
        
	
		public string Enhet { get; set; }
		public Kontostreng Kontering { get; set; }
		public long Pris { get; set; }
		
	}
}
