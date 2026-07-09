using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Faktura
{

    public class FakturaResource 
    {

    
        public AdresseResource Adresse { get; set; }
        public long Belop { get; set; }
        public bool? Betalt { get; set; }
        public DateTime Dato { get; set; }
        public Identifikator Fakturanummer { get; set; }
        public bool? Fakturert { get; set; }
        public DateTime Forfallsdato { get; set; }
        public bool? Kreditert { get; set; }
        public string Mottaker { get; set; }
        public long? Restbelop { get; set; }
        
        public FakturaResource()
        {
            Links = new Dictionary<string, List<Link>>();
        }

        [JsonProperty(PropertyName = "_links")]
        public Dictionary<string, List<Link>> Links { get; private set; }

        protected void AddLink(string key, Link link)
        {
            if (!Links.ContainsKey(key))
            {
                Links.Add(key, new List<Link>());
            }
            Links[key].Add(link);
        }
     
            

        public void AddFakturagrunnlag(Link link)
        {
            AddLink("fakturagrunnlag", link);
        }
    }
}
