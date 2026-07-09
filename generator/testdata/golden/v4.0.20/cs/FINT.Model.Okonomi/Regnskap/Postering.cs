using System;
using System.Collections.Generic;



using FINT.Model.Administrasjon.Kompleksedatatyper;
using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Regnskap
{
	public class Postering {
		public enum Relasjonsnavn
        {
			TRANSAKSJON
        }
        
	
		public long Belop { get; set; }
		public bool Debet { get; set; }
		public Kontostreng Kontering { get; set; }
		public Identifikator PosteringsId { get; set; }
		
	}
}
