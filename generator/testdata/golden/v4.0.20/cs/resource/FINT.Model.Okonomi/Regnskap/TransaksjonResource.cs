using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Regnskap
{

    public class TransaksjonResource 
    {

    
        public long Belop { get; set; }
        public string Beskrivelse { get; set; }
        public List<Bilag> Bilag { get; set; }
        public DateTime Forfallsdato { get; set; }
        public DateTime? Oppdateringstidspunkt { get; set; }
        public Identifikator TransaksjonsId { get; set; }
        public DateTime? Transaksjonstidspunkt { get; set; }
        
        public TransaksjonResource()
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
     
            

        public void AddLeverandor(Link link)
        {
            AddLink("leverandor", link);
        }

        public void AddAnsvarlig(Link link)
        {
            AddLink("ansvarlig", link);
        }

        public void AddValuta(Link link)
        {
            AddLink("valuta", link);
        }

        public void AddPostering(Link link)
        {
            AddLink("postering", link);
        }
    }
}
