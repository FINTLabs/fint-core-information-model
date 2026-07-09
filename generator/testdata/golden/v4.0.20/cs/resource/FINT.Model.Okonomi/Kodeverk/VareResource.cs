using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Administrasjon.Kompleksedatatyper;
using FINT.Model.Felles.Basisklasser;

namespace FINT.Model.Okonomi.Kodeverk
{

    public class VareResource : Begrep 
    {

    
        public string Enhet { get; set; }
        public KontostrengResource Kontering { get; set; }
        public long Pris { get; set; }
        
        public VareResource()
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
     
            

        public void AddFakturautsteder(Link link)
        {
            AddLink("fakturautsteder", link);
        }

        public void AddMerverdiavgift(Link link)
        {
            AddLink("merverdiavgift", link);
        }
    }
}
