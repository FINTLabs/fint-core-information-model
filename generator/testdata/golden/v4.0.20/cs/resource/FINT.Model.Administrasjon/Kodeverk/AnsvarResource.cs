using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Administrasjon.Kodeverk
{

    public class AnsvarResource : Kontodimensjon 
    {

    
        public AnsvarResource()
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
     
            

        public void AddOverordnet(Link link)
        {
            AddLink("overordnet", link);
        }

        public void AddUnderordnet(Link link)
        {
            AddLink("underordnet", link);
        }

        public void AddOrganisasjonselement(Link link)
        {
            AddLink("organisasjonselement", link);
        }
    }
}
