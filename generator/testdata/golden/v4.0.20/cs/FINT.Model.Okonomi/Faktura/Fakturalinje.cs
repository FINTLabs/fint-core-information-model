using System;
using System.Collections.Generic;



namespace FINT.Model.Okonomi.Faktura
{
	public class Fakturalinje {
		public enum Relasjonsnavn
        {
			VARE
        }
        
	
		public float Antall { get; set; }
		public List<string> Fritekst { get; set; }
		public long Pris { get; set; }
		
	}
}
