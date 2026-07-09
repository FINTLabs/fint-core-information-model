using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Timeplan
{

    public class EksamenResource 
    {

    
        public string Beskrivelse { get; set; }
        public string Navn { get; set; }
        public DateTime? Oppmotetidspunkt { get; set; }
        public Identifikator SystemId { get; set; }
        public Periode Tidsrom { get; set; }
        
        public EksamenResource()
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
     
            

        public void AddRom(Link link)
        {
            AddLink("rom", link);
        }

        public void AddEksamensgruppe(Link link)
        {
            AddLink("eksamensgruppe", link);
        }
    }
}
