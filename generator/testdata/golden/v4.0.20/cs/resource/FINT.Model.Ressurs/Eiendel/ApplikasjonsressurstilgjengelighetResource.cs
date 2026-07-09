using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Ressurs.Eiendel
{

    public class ApplikasjonsressurstilgjengelighetResource 
    {

    
        public Periode Gyldighetsperiode { get; set; }
        public long? Lisensantall { get; set; }
        public Identifikator SystemId { get; set; }
        
        public ApplikasjonsressurstilgjengelighetResource()
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
     
            

        public void AddKonsument(Link link)
        {
            AddLink("konsument", link);
        }

        public void AddRessurs(Link link)
        {
            AddLink("ressurs", link);
        }
    }
}
