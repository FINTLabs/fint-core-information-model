using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Faktura
{

    public class FakturautstederResource 
    {

    
        public string Navn { get; set; }
        public Identifikator SystemId { get; set; }
        
        public FakturautstederResource()
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
     
            

        public void AddOrganisasjonselement(Link link)
        {
            AddLink("organisasjonselement", link);
        }

        public void AddFakturagrunnlag(Link link)
        {
            AddLink("fakturagrunnlag", link);
        }

        public void AddVare(Link link)
        {
            AddLink("vare", link);
        }
    }
}
