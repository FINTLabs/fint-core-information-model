using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Personvern.Samtykke
{

    public class SamtykkeResource 
    {

    
        public Periode Gyldighetsperiode { get; set; }
        public DateTime Opprettet { get; set; }
        public Identifikator SystemId { get; set; }
        
        public SamtykkeResource()
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
     
            

        public void AddBehandling(Link link)
        {
            AddLink("behandling", link);
        }

        public void AddPerson(Link link)
        {
            AddLink("person", link);
        }

        public void AddOrganisasjonselement(Link link)
        {
            AddLink("organisasjonselement", link);
        }
    }
}
