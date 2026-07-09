using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Administrasjon.Personal
{
	public class Fravar {
		public enum Relasjonsnavn
        {
			FRAVARSGRUNN,
			FRAVARSTYPE,
			ARBEIDSFORHOLD,
			FORTSETTELSE,
			GODKJENNER,
			FORTSETTER
        }
        
	
		public DateTime? Godkjent { get; set; }
		public Identifikator KildesystemId { get; set; }
		public Periode Periode { get; set; }
		public long Prosent { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
